package com.license.outside_issues.web.api;

import com.license.outside_issues.service.issue.IssueService;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
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
    public ResponseEntity<?> getAllIssues(@RequestParam(required = false, defaultValue = "false") Boolean hasLocation) {
        return ResponseEntity.ok(issueService.getAllIssues(hasLocation));
    }

    @GetMapping("/basic-statistics")
    public ResponseEntity<List<StatisticsDTO>> getBasicStatistics(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(issueService.getBasicStatistics(email));
    }

    @PostMapping
    public ResponseEntity<Long> addIssue(@RequestBody IssueDTO issue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueService.addIssue(issue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueDTO> updateIssue(@PathVariable Long id, @RequestParam(required = false) String type,
                                                @RequestParam(required = false) String state) {
        return ResponseEntity.ok(issueService.updateIssue(id, type, state));
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<IssueDTO>> findIssues(@RequestParam(required = false) String type,
                                                     @RequestParam(required = false) String state,
                                                     @RequestParam(required = false) String fromDate,
                                                     @RequestParam(required = false) String toDate,
                                                     @RequestParam(required = false, defaultValue = "false") boolean hasLocation,
                                                     Pageable pageable) {
        return ResponseEntity.ok(issueService.findIssues(type, state, fromDate, toDate, hasLocation, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteIssue(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.deleteIssue(id));
    }
}
