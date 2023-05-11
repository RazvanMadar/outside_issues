package com.license.outside_issues.web.api;

import com.license.outside_issues.model.WebSocketMessage;
import com.license.outside_issues.model.WebSocketMessageUpdate;
import com.license.outside_issues.service.message.MessageService;
import com.license.outside_issues.service.message.dtos.MessageDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(WebSocketController.class.toString());

    public WebSocketController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload WebSocketMessage message) {
        template.convertAndSend("/topic/message", message);
    }

    @MessageMapping("/send")
    public void receivePrivateMessage(@Payload WebSocketMessage message) {
        System.out.println(message);
        template.convertAndSendToUser(message.getToEmail(), "/private", message);
    }

    @PostMapping("/send-message")
    public ResponseEntity<Long> sendMessage(@RequestBody WebSocketMessage message) {
        template.convertAndSendToUser(message.getToEmail(), "/private", message);
        return ResponseEntity.ok(messageService.sendMessage(new MessageDTO(message.getMessage(), message.getFromEmail(), message.getToEmail())));
    }

    @PostMapping("/send-update")
    public ResponseEntity<Void> sendUpdate(@RequestBody List<WebSocketMessageUpdate> messages) {
        messages.forEach(message -> template.convertAndSendToUser(message.getTo(), "/private", message));
        return ResponseEntity.noContent().build();
    }

    @SendTo("/topic/message")
    public WebSocketMessage broadcastMessage(@Payload WebSocketMessage message) {
        return message;
    }
}
