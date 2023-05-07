package com.license.outside_issues.service.issue;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues(Boolean hasLocation);
    List<StatisticsDTO> getBasicStatistics(String email);
    List<StatisticsDTO> getYearStatistics(String year);
    Long addIssue(IssueDTO issue);
    IssueDTO findById(Long id);
    IssueDTO updateIssue(Long id, String type, String state);
    Long deleteIssue(Long id);
    Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable);
    Page<IssueDTO> findAllByCitizenEmail(String email, Pageable pageable);
}
