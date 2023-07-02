package com.license.outside_issues.controller.api;

import com.license.outside_issues.dto.WebSocketMessageDTO;
import com.license.outside_issues.dto.WebSocketMessageUpdateDTO;
import com.license.outside_issues.service.message.MessageService;
import com.license.outside_issues.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class WebSocketController {
    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    public WebSocketController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/sendMessage")
    public ResponseEntity<Void> receiveMessage(@Payload WebSocketMessageDTO message) {
        template.convertAndSend("/topic/message", message);
        return null;
    }

    @MessageMapping("/send")
    public ResponseEntity<Void> receivePrivateMessage(@Payload WebSocketMessageDTO message) {
        template.convertAndSendToUser(message.getToEmail(), "/private", message);
        return null;
    }

    @PostMapping("/send-message")
    public ResponseEntity<Long> sendMessage(@RequestBody WebSocketMessageDTO message) {
        final Long messageId = messageService.sendMessage(new MessageDTO(message.getMessage(), message.getFromEmail(), message.getToEmail()));
        template.convertAndSendToUser(message.getToEmail(), "/private", message);
        return ResponseEntity.ok(messageId);
    }

    @PostMapping("/send-update")
    public ResponseEntity<Void> sendUpdate(@RequestBody List<WebSocketMessageUpdateDTO> messages) {
        messages.forEach(message -> template.convertAndSendToUser(message.getTo(), "/private", message));
        return ResponseEntity.noContent().build();
    }

    @SendTo("/topic/message")
    public WebSocketMessageDTO broadcastMessage(@Payload WebSocketMessageDTO message) {
        return message;
    }
}
