package com.license.outside_issues.repository;

import com.license.outside_issues.common.PaginationUtil;
import com.license.outside_issues.service.blacklist.BlacklistService;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class CitizenJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BlacklistService blacklistService;

    public CitizenJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, BlacklistService blacklistService) {
        this.jdbcTemplate = jdbcTemplate;
        this.blacklistService = blacklistService;
    }

    public Page<DisplayCitizenDTO> findCitizens(String email, Pageable pageable) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder query = new StringBuilder("SELECT * FROM citizens ");
        if (email != null && !email.isBlank() && !email.isEmpty()) {
            query.append("WHERE email LIKE '%").append(email).append("%'");
            parameters.addValue("email", email);
        }
        System.out.println(query);

        Integer filteredCitizensSql = jdbcTemplate.queryForObject(query.toString().replace("*", "COUNT(*)"), parameters, Integer.class);
        int filteredCitizensSize = Objects.requireNonNullElse(filteredCitizensSql, 0);

        final List<Sort.Order> orders = pageable.getSort().get().toList();
        System.out.println(orders);
        final String queryOrder = PaginationUtil.createOrderQuery(orders);
        query.append(queryOrder);
        final String pagination = PaginationUtil.createPaginationQuery(pageable);
        query.append(pagination);

        List<DisplayCitizenDTO> citizenDTOS = jdbcTemplate.query(query.toString(), parameters, (rs, rowNum) -> mapToCitizenDTO(rs));
        System.out.println(citizenDTOS);

        return new PageImpl<>(citizenDTOS, pageable, filteredCitizensSize);
    }

    private DisplayCitizenDTO mapToCitizenDTO(ResultSet rs) throws SQLException {
        DisplayCitizenDTO displayCitizenDTO = new DisplayCitizenDTO();
        displayCitizenDTO.setId(rs.getLong("id"));
        displayCitizenDTO.setEmail(rs.getString("email"));
        displayCitizenDTO.setFirstName(rs.getString("first_name"));
        displayCitizenDTO.setLastName(rs.getString("last_name"));
        displayCitizenDTO.setPhoneNumber(rs.getString("phone_number"));
        final boolean isCitizenBlocked = blacklistService.isCitizenBlocked(rs.getLong("id"));
        displayCitizenDTO.setBlocked(isCitizenBlocked);
        return displayCitizenDTO;
    }
}