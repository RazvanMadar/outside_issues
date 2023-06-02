package com.license.outside_issues.controller.api;

import com.license.outside_issues.service.reactions.CitizenReactionsService;
import com.license.outside_issues.dto.CitizenReactionsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/citizen-reactions")
public class CitizenReactionsResource {
    private final CitizenReactionsService citizenReactionsService;

    public CitizenReactionsResource(CitizenReactionsService citizenReactionsService) {
        this.citizenReactionsService = citizenReactionsService;
    }

    @GetMapping
    public ResponseEntity<?> getReactionsForSomeCitizenAndIssue(@RequestParam(required = false) Long citizenId, @RequestParam(required = false) Long issueId) {
        return ResponseEntity.ok(citizenReactionsService.findByCitizenIdAndIssueId(citizenId, issueId));
    }

    @PostMapping
    public ResponseEntity<?> addCitizenReaction(@RequestBody List<CitizenReactionsDTO> citizenReactionsDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenReactionsService.addCitizenReaction(citizenReactionsDTOs));
    }
}
