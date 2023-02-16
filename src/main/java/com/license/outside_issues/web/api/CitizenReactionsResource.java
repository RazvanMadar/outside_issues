package com.license.outside_issues.web.api;

import com.license.outside_issues.service.reactions.CitizenReactionsService;
import com.license.outside_issues.service.reactions.dtos.CitizenReactionsDTO;
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

    @GetMapping()
    public ResponseEntity<?> getReactionsForSomeCitizenAndIssue(@RequestParam Long citizenId, @RequestParam Long issueId) {
        return ResponseEntity.ok(citizenReactionsService.findByCitizenIdAndIssueId(citizenId, issueId));
    }

    @PostMapping
    public ResponseEntity<?> addCitizenReaction(@RequestBody List<CitizenReactionsDTO> citizenReactionsDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenReactionsService.addCitizenReaction(citizenReactionsDTOs));
    }
}
