package com.license.outside_issues.service.issue;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues(Boolean hasLocation);
    Long addIssue(IssueDTO issue);
    IssueDTO findById(Long id);
    IssueDTO updateIssue(Long id, String type, String state);
    Long deleteIssue(Long id);
    Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable);
}
