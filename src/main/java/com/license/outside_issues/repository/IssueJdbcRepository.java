package com.license.outside_issues.repository;

import com.license.outside_issues.common.PaginationUtil;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.mapper.IssueMapper;
import com.license.outside_issues.model.Address;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IssueJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CitizenRepository citizenRepository;

    public IssueJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, CitizenRepository citizenRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.citizenRepository = citizenRepository;
    }


    public Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, Pageable pageable) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();

//        String actualFromDate = fromDate.toString().split("/+")[0];
//        actualFromDate = actualFromDate.substring(0, actualFromDate.length() - 1);
//        String actualToDate = toDate.toString().split("/+")[0];
//        actualToDate = actualToDate.substring(0, actualToDate.length() - 1);
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

        if (fromDate != null && toDate != null) {
//            BigDecimal fromDateDecimal = new BigDecimal(fromDate.format(DateTimeFormatter.ISO_DATE));
//            BigDecimal toDateDecimal = new BigDecimal(toDate.format(DateTimeFormatter.ISO_DATE));
            query.append(isConditionPresent ? "AND " : "WHERE ");
            query.append("reported_date >= :from_date AND reported_date <= :to_date ");
            parameters.addValue("from_date", LocalDate.parse(fromDate));
            parameters.addValue("to_date", LocalDate.parse(toDate));

//            parameters.addValue("from_date", fromDateDecimal);
//            parameters.addValue("to_date", toDateDecimal);
        }

//        else if (fromDate != null) {
//            BigDecimal fromDateDecimal = new BigDecimal(fromDate.format(DateTimeFormatter.ISO_DATE));
//            query.append(isConditionPresent ? "AND " : "WHERE ");
//            query.append("reported_date >= :from_date ");
//            parameters.addValue("from_date", fromDateDecimal);
//        }
//        else if (toDate != null) {
//            BigDecimal toDateDecimal = new BigDecimal(toDate.format(DateTimeFormatter.ISO_DATE));
//            query.append(isConditionPresent ? "AND " : "WHERE ");
//            query.append("reported_date <= :to_date ");
//            parameters.addValue("to_date", toDateDecimal);
//        }

        System.out.println(query);

        final List<Sort.Order> orders = pageable.getSort().get().toList();
        System.out.println(orders);
        final String queryOrder = PaginationUtil.createOrderQuery(orders);
        query.append(queryOrder);
        final String pagination = PaginationUtil.createPaginationQuery(pageable);
        query.append(pagination);

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

        return new PageImpl<>(issues, pageable, issues.size());
    }

    private IssueDTO mapToIssuesDTO(ResultSet rs) throws SQLException {
        IssueDTO issue = new IssueDTO();
        issue.setId(rs.getLong("id"));
        issue.setType(IssueType.valueOf(rs.getString("type")));
        issue.setState(IssueState.valueOf(rs.getString("state")));
        issue.setReportedDate(new Date(rs.getDate("reported_date").getTime()).toLocalDate());

//        issue.setFromDate("start_date", LocalDate.class);
//        issue.setToDate("to_date", LocalDate.class);
        return issue;
    }

    private List<IssueDTO> convertIssuesToDTOS(List<Issue> issues) {
        return issues.stream()
                .map(IssueMapper.INSTANCE::modelToDto)
                .collect(Collectors.toList());
    }
}
