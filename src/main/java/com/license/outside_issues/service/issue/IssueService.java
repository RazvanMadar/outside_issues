package com.license.outside_issues.service.issue;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues();
    Long addIssue(IssueDTO issue);
    List<Issue> findIssuesByDescription(String description);
    Page<IssueDTO> findIssues(String type, String state, java.sql.Date date, Pageable pageable);
}
