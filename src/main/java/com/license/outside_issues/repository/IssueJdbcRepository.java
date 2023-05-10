package com.license.outside_issues.repository;

import com.license.outside_issues.common.PaginationUtil;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.service.issue.dtos.AddressDTO;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class IssueJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CitizenRepository citizenRepository;
    private final IssueRepository issueRepository;

    public IssueJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, CitizenRepository citizenRepository, IssueRepository issueRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.citizenRepository = citizenRepository;
        this.issueRepository = issueRepository;
    }

    public List<StatisticsDTO> getBasicStatistics(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT state, COUNT(state) AS val FROM issues ";
        if (email != null) {
            query += "WHERE citizen_email = " + "'" + email + "'";
        }
        query += " GROUP BY state";
        List<StatisticsDTO> statistics = jdbcTemplate.query(query, parameters, (rs, rowNum) -> mapToStatisticsDTO(rs));
        fillWithZeroIfNoIssuesReported(statistics, "REGISTERED");
        fillWithZeroIfNoIssuesReported(statistics, "PLANNED");
        fillWithZeroIfNoIssuesReported(statistics, "WORKING");
        fillWithZeroIfNoIssuesReported(statistics, "SOLVED");
        return statistics;
    }

    public List<StatisticsDTO> getTypeStatistics() {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT type as state, COUNT(*) as val " +
                "FROM issues GROUP BY type";

        return jdbcTemplate.query(query, parameters, (rs, rowNum) -> mapToStatisticsDTO(rs));
    }

    public List<StatisticsDTO> getYearStatistics(String year) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "SELECT EXTRACT(MONTH FROM reported_date) as state, COUNT(*) as val " +
                "FROM issues WHERE EXTRACT(YEAR FROM reported_date) = " + year +
                " GROUP BY EXTRACT(MONTH FROM reported_date) ORDER BY EXTRACT(MONTH FROM reported_date)";

        List<StatisticsDTO> statistics = jdbcTemplate.query(query, parameters, (rs, rowNum) -> mapToStatisticsDTO(rs));
        fillWithZeroIfNoIssuesReported(statistics, "1");
        fillWithZeroIfNoIssuesReported(statistics, "2");
        fillWithZeroIfNoIssuesReported(statistics, "3");
        fillWithZeroIfNoIssuesReported(statistics, "4");
        fillWithZeroIfNoIssuesReported(statistics, "5");
        fillWithZeroIfNoIssuesReported(statistics, "6");
        fillWithZeroIfNoIssuesReported(statistics, "7");
        fillWithZeroIfNoIssuesReported(statistics, "8");
        fillWithZeroIfNoIssuesReported(statistics, "9");
        fillWithZeroIfNoIssuesReported(statistics, "10");
        fillWithZeroIfNoIssuesReported(statistics, "11");
        fillWithZeroIfNoIssuesReported(statistics, "12");

        for (StatisticsDTO statisticsDTO : statistics) {
            if (statisticsDTO.getState().length() == 1) {
                statisticsDTO.setState("0" + statisticsDTO.getState());
            }
        }
        statistics.sort(Comparator.comparing(StatisticsDTO::getState));
        return statistics;
    }

    public Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder query = new StringBuilder("SELECT * FROM issues ");
        boolean isConditionPresent = false;
        if (type != null) {
            query.append("WHERE type = :type ");
            isConditionPresent = true;
            parameters.addValue("type", type);
        }
        if (state != null) {
            query.append(isConditionPresent ? "AND " : "WHERE ");
            query.append("state = :state ");
            isConditionPresent = true;
            parameters.addValue("state", state);
        }

        if (fromDate != null) {
            query.append(isConditionPresent ? "AND " : "WHERE ");
            query.append("reported_date >= :from_date ");
            parameters.addValue("from_date", LocalDate.parse(fromDate));
        }
        if (toDate != null) {
            query.append(isConditionPresent ? "AND " : "WHERE ");
            query.append("reported_date <= :to_date ");
            parameters.addValue("to_date", LocalDate.parse(toDate));
        }
        if (hasLocation) {
            query.append(isConditionPresent ? "AND " : "WHERE ");
            query.append("has_location = true ");
        }

        System.out.println(query);
        Integer filteredIssuesSql = jdbcTemplate.queryForObject(query.toString().replace("*", "COUNT(*)"), parameters, Integer.class);
        int filteredIssuesSize = Objects.requireNonNullElse(filteredIssuesSql, 0);

        final List<Sort.Order> orders = pageable.getSort().get().toList();
        System.out.println(orders);
        final String queryOrder = PaginationUtil.createOrderQuery(orders);
        query.append(queryOrder);
        final String pagination = PaginationUtil.createPaginationQuery(pageable);
        query.append(pagination);

        System.out.println(query);
//        List<Issue> issues = new ArrayList<>();
//                jdbcTemplate.query(query.toString(), parameters, (rs, rowNum) ->
//                new Issue(
//                        rs.getLong("id"), IssueType.valueOf(rs.getString("type")),
//                        new Address(rs.getDouble("lat"),
//                        rs.getDouble("lng")),
//                        IssueState.valueOf(rs.getString("state")),
//                        new Date(rs.getDate("reported_date").getTime()).toLocalDate(),
//                        rs.getInt("likes_number"), rs.getInt("dislikes_number"),
//                        rs.getString("description"),
//                        Boolean.TRUE, new HashSet<>(),
//                        citizenRepository.findById(rs.getLong("citizen_id")).get())
//        );
//        List<IssueDTO> issueDTOS = convertIssuesToDTOS(issues);
        List<IssueDTO> issues = jdbcTemplate.query(query.toString(), parameters, (rs, rowNum) -> mapToIssuesDTO(rs));
        System.out.println(issues);

        return new PageImpl<>(issues, pageable, filteredIssuesSize);
    }

    private IssueDTO mapToIssuesDTO(ResultSet rs) throws SQLException {
        IssueDTO issue = new IssueDTO();
        issue.setId(rs.getLong("id"));
        issue.setType(IssueType.valueOf(rs.getString("type")));
        issue.setState(IssueState.valueOf(rs.getString("state")));
        issue.setReportedDate(new Date(rs.getDate("reported_date").getTime()).toLocalDate());
        issue.setDescription(rs.getString("description"));
        issue.setActualLocation(rs.getString("actual_location"));
        issue.setAddress(new AddressDTO(rs.getDouble("lat"), rs.getDouble("lng")));
        issue.setLikesNumber(rs.getInt("likes_number"));
        issue.setHasLocation(rs.getBoolean("has_location"));
        issue.setDislikesNumber(rs.getInt("dislikes_number"));
        issue.setCitizenEmail(rs.getString("citizen_email"));

//        issue.setFromDate("start_date", LocalDate.class);
//        issue.setToDate("to_date", LocalDate.class);
        return issue;
    }

    private StatisticsDTO getStatisticsFromIndex(int index, int val) {
        StatisticsDTO statistics = new StatisticsDTO();
        statistics.setState(Month.of(index).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        statistics.setVal(val);
        return statistics;
    }

    private StatisticsDTO mapToStatisticsDTO(ResultSet rs) throws SQLException {
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setState(rs.getString("state"));
        statisticsDTO.setVal(rs.getInt("val"));

        return statisticsDTO;
    }

    private void fillWithZeroIfNoIssuesReported(List<StatisticsDTO> statisticsDTOS, String state) {
        boolean exists = false;
        for (StatisticsDTO statisticsDTO : statisticsDTOS) {
            if (state.equals(statisticsDTO.getState())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            statisticsDTOS.add(new StatisticsDTO(state, 0));
        }
    }
}
