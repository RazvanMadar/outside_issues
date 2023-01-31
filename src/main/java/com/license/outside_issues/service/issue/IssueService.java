package com.license.outside_issues.service.issue;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues(Boolean hasLocation);
    Long addIssue(IssueDTO issue);
    List<Issue> findIssuesByDescription(String description);
    Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, Pageable pageable);
}
