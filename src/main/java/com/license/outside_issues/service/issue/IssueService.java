package com.license.outside_issues.service.issue;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IssueService {
    List<Issue> getAllIssues(Boolean hasLocation);
    Long addIssue(IssueDTO issue);
    Boolean addReactionsForIssues(Map<Long, Integer> issues);
    Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable);
}
