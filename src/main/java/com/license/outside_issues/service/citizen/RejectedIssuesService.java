package com.license.outside_issues.service.citizen;

import com.license.outside_issues.service.issue.dtos.StatisticsDTO;

import java.util.List;

public interface RejectedIssuesService {
    Long addRejected(String email);
    long getRejectedForCitizen(Long citizenId);
    List<StatisticsDTO> getAllRejected();
    List<StatisticsDTO> getAllRejectedForCitizen(Long id, String email);
}
