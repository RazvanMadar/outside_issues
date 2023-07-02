package com.license.outside_issues.controller.api;

import com.license.outside_issues.dto.EmailMessageDTO;
import com.license.outside_issues.service.email.EmailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class EmailController {
    private final EmailSender emailSender;

    public EmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailMessageDTO emailMessage) {
        emailSender.sendEmail(emailMessage);
        return null;
    }
}
