package com.license.outside_issues.controller;

import com.license.outside_issues.model.Issue;
import com.license.outside_issues.service.issue.IssueService;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/issues")
public class IssueResource {
    private final IssueService issueService;

    public IssueResource(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public ResponseEntity<?> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @PostMapping
    public ResponseEntity<Long> addIssue(@RequestBody IssueDTO issue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.addIssue(issue));
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<IssueDTO>> findIssues(@RequestParam(required = false) String type,
                                                          @RequestParam(required = false) String state,
                                                          @RequestParam(required = false) java.sql.Date date,
                                                          Pageable pageable) {
        return ResponseEntity.ok(issueService.findIssues(type, state, date, pageable));
    }
}
