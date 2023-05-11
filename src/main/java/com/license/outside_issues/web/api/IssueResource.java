package com.license.outside_issues.web.api;

import com.license.outside_issues.model.WebSocketMessageUpdate;
import com.license.outside_issues.service.citizen.CitizenService;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.issue.IssueService;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/issues")
public class IssueResource {
    private final IssueService issueService;
    private final CitizenService citizenService;
    private final WebSocketController webSocketController;

    public IssueResource(IssueService issueService, CitizenService citizenService, WebSocketController webSocketController) {
        this.issueService = issueService;
        this.citizenService = citizenService;
        this.webSocketController = webSocketController;
    }

    @GetMapping
    public ResponseEntity<?> getAllIssues(@RequestParam(required = false, defaultValue = "false") Boolean hasLocation) {
        return ResponseEntity.ok(issueService.getAllIssues(hasLocation));
    }

    @GetMapping("/basic-statistics")
    public ResponseEntity<List<StatisticsDTO>> getBasicStatistics(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(issueService.getBasicStatistics(email));
    }

    @GetMapping("/year-statistics")
    public ResponseEntity<List<StatisticsDTO>> getYearStatistics(@RequestParam(defaultValue = "2023") String year) {
        return ResponseEntity.ok(issueService.getYearStatistics(year));
    }

    @GetMapping("/type-statistics")
    public ResponseEntity<List<StatisticsDTO>> getTypeStatistics() {
        return ResponseEntity.ok(issueService.getTypeStatistics());
    }

    @PostMapping
    public ResponseEntity<Long> addIssue(@RequestBody IssueDTO issue) {
        Long issueId = issueService.addIssue(issue);
        sendMessagesViaWebSocketOnUpdate(citizenService.findAllUsersExceptOne(issue.getCitizenEmail()));
        return ResponseEntity.status(HttpStatus.CREATED).body(issueId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueDTO> updateIssue(@PathVariable Long id, @RequestParam(required = false) String type,
                                                @RequestParam(required = false) String state) {
        final IssueDTO issueDTO = issueService.updateIssue(id, type, state);
        sendMessagesViaWebSocketOnUpdate(citizenService.findAllCitizenUsers().stream().map(DisplayCitizenDTO::getEmail).collect(Collectors.toList()));
        return ResponseEntity.ok(issueDTO);
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

    @GetMapping("/email/{email}")
    public ResponseEntity<Page<IssueDTO>> findIssues(@PathVariable String email, Pageable pageable) {
        return ResponseEntity.ok(issueService.findAllByCitizenEmail(email, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteIssue(@PathVariable Long id) {
        final Long deletedIssueId = issueService.deleteIssue(id);
        sendMessagesViaWebSocketOnUpdate(citizenService.findAllCitizenUsers().stream().map(DisplayCitizenDTO::getEmail).collect(Collectors.toList()));
        return ResponseEntity.ok(deletedIssueId);
    }

    private void sendMessagesViaWebSocketOnUpdate(List<String> emails) {
        final List<WebSocketMessageUpdate> webSocketMessageUpdates = emails.stream()
                .map(WebSocketMessageUpdate::new)
                .collect(Collectors.toList());
        webSocketController.sendUpdate(webSocketMessageUpdates);
    }
}
