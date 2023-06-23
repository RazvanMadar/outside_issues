package com.license.outside_issues.controller.api;

import com.license.outside_issues.dto.ChatCitizenDTO;
import com.license.outside_issues.service.message.MessageService;
import com.license.outside_issues.dto.MessageDTO;
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
    public ResponseEntity<MessageDTO> findLatestMessageForCitizen(@RequestParam Long fromId, @RequestParam Long toId) {
        return ResponseEntity.ok(messageService.findLatestMessageForCitizen(fromId, toId));
    }

    @PostMapping
    public ResponseEntity<Long> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(messageDTO));
    }

    @GetMapping("/role")
    public ResponseEntity<List<ChatCitizenDTO>> getChatUsersByRole(@RequestParam String name) {
        return ResponseEntity.ok(messageService.getChatUsersByRole(name));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MessageDTO> findLatestMessageByEmail(@PathVariable String email) {
        return ResponseEntity.ok(messageService.findLatestMessageByEmail(email));
    }
}
