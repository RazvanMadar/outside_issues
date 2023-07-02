package com.license.outside_issues.service.email;

import com.license.outside_issues.dto.EmailMessageDTO;

public interface EmailSender {
    void sendEmail(EmailMessageDTO emailMessage);
}
