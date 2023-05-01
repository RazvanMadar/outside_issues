package com.license.outside_issues.web.api;

import com.license.outside_issues.service.citizen.RejectedIssuesService;
import com.license.outside_issues.service.issue.dtos.RejectedIssuesDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/rejected")
@RestController
public class RejectedIssuesResource {
    private final RejectedIssuesService rejectedIssuesService;

    public RejectedIssuesResource(RejectedIssuesService rejectedIssuesService) {
        this.rejectedIssuesService = rejectedIssuesService;
    }

    @PostMapping()
    public ResponseEntity<Long> addRejected(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rejectedIssuesService.addRejected(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Long> getRejectedForCitizen(@PathVariable Long id) {
        return ResponseEntity.ok(rejectedIssuesService.getRejectedForCitizen(id));
    }

    @GetMapping
    public ResponseEntity<List<StatisticsDTO>> getAllRejected() {
        return ResponseEntity.ok(rejectedIssuesService.getAllRejected());
    }

    @GetMapping("/citizen")
    public ResponseEntity<List<StatisticsDTO>> getAllRejectedForCitizen(@RequestParam Long id, @RequestParam String email) {
        return ResponseEntity.ok(rejectedIssuesService.getAllRejectedForCitizen(id, email));
    }
}
