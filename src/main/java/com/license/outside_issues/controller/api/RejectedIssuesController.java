package com.license.outside_issues.controller.api;

import com.license.outside_issues.service.citizen.RejectedIssuesService;
import com.license.outside_issues.dto.StatisticsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/rejected")
@RestController
public class RejectedIssuesController {
    private final RejectedIssuesService rejectedIssuesService;

    public RejectedIssuesController(RejectedIssuesService rejectedIssuesService) {
        this.rejectedIssuesService = rejectedIssuesService;
    }

    @PostMapping
    public ResponseEntity<Long> addRejected(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rejectedIssuesService.addRejected(email));
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
