package com.license.outside_issues.service.email;

import com.license.outside_issues.dto.EmailMessage;

public interface EmailSender {
    void sendEmail(EmailMessage emailMessage);
}
