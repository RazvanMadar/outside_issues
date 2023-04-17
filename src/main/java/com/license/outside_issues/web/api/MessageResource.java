package com.license.outside_issues.web.api;

import com.license.outside_issues.service.issue.dtos.IssueDTO;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import com.license.outside_issues.service.message.MessageService;
import com.license.outside_issues.service.message.dtos.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/messages")
public class MessageResource {
    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MessageDTO>> getMessagesFromCitizen(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageForCitizen(id));
    }

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getChatMessages(@RequestParam Long from, @RequestParam Long to) {
        return ResponseEntity.ok(messageService.getChatMessages(from, to));
    }

    @GetMapping("/latest")
    public ResponseEntity<MessageDTO> getLatestMessage(@RequestParam Long from, @RequestParam Long to) {
        return ResponseEntity.ok(messageService.getLatestMessage(from, to));
    }

    @PostMapping
    public ResponseEntity<Long> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(messageDTO));
    }
}
