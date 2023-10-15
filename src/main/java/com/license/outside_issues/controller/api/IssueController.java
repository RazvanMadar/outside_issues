package com.license.outside_issues.controller.api;

import com.license.outside_issues.dto.WebSocketMessageUpdateDTO;
import com.license.outside_issues.service.citizen.CitizenService;
import com.license.outside_issues.dto.DisplayCitizenDTO;
import com.license.outside_issues.service.issue.IssueService;
import com.license.outside_issues.dto.IssueDTO;
import com.license.outside_issues.dto.StatisticsDTO;
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
public class IssueController {
    private final IssueService issueService;
    private final CitizenService citizenService;
    private final WebSocketController webSocketController;

    public IssueController(IssueService issueService, CitizenService citizenService, WebSocketController webSocketController) {
        this.issueService = issueService;
        this.citizenService = citizenService;
        this.webSocketController = webSocketController;
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
    public ResponseEntity<List<StatisticsDTO>> getTypeStatistics(@RequestParam(required = false) String email) {
        return ResponseEntity.ok(issueService.getTypeStatistics(email));
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
                                                     @RequestParam(required = false, defaultValue = "false") boolean all,
                                                     Pageable pageable) {
        return ResponseEntity.ok(issueService.findIssues(type, state, fromDate, toDate, hasLocation, all, pageable));
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
        final List<WebSocketMessageUpdateDTO> webSocketMessageUpdates = emails.stream()
                .map(WebSocketMessageUpdateDTO::new)
                .collect(Collectors.toList());
        webSocketController.sendUpdate(webSocketMessageUpdates);
    }
}
