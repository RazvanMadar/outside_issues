package com.license.outside_issues.repository;

import com.license.outside_issues.common.PaginationUtil;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.mapper.IssueMapper;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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


    public Page<IssueDTO> findIssues(String type, String state, java.sql.Date date, Pageable pageable) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("type", type);
        parameters.addValue("state", state);
        parameters.addValue("date", date);

        StringBuilder query = new StringBuilder("SELECT * FROM issues");
        boolean isConditionPresent = false;
        if (null != type) {
            query.append(" WHERE type = :type");
            isConditionPresent = true;
        }
        if (null != state) {
            query.append(isConditionPresent ? " AND" : " WHERE");
            query.append(" state = :state");
            isConditionPresent = true;
        }
        if (null != date) {
            query.append(isConditionPresent ? " AND" : " WHERE");
            query.append(" reported_date = :date");
        }
        System.out.println(query);

        final List<Sort.Order> orders = pageable.getSort().get().toList();
        System.out.println(orders);
        final String queryOrder = PaginationUtil.createOrderQuery(orders);
        query.append(queryOrder);
        final String pagination = PaginationUtil.createPaginationQuery(pageable);
        query.append(pagination);

        List<Issue> issues = jdbcTemplate.query(query.toString(), parameters, (rs, rowNum) ->
                new Issue(
                        rs.getLong("id"), rs.getString("photo"),
                        IssueType.valueOf(rs.getString("type")), rs.getDouble("latitude"),
                        rs.getDouble("longitude"), IssueState.valueOf(rs.getString("state")),
                        new Date(rs.getDate("reported_date").getTime()).toLocalDate(),
                        rs.getInt("likes_number"), rs.getInt("dislikes_number"),
                        rs.getString("description"),
                        citizenRepository.findById(rs.getLong("citizen_id")).get())
        );
        List<IssueDTO> issueDTOS = convertIssuesToDTOS(issues);

        return new PageImpl<>(issueDTOS, pageable, issueDTOS.size());
    }

    private List<IssueDTO> convertIssuesToDTOS(List<Issue> issues) {
        return issues.stream()
                .map(IssueMapper.INSTANCE::modelToDto)
                .collect(Collectors.toList());
    }
}
