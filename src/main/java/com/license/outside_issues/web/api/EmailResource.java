package com.license.outside_issues.web.api;

import com.license.outside_issues.service.email.EmailMessage;
import com.license.outside_issues.service.email.EmailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class EmailResource {
    private final EmailSender emailSender;

    public EmailResource(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailMessage emailMessage) {
        emailSender.sendEmail(emailMessage);
        return null;
    }
}
