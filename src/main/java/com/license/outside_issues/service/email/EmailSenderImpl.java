package com.license.outside_issues.service.email;

import com.license.outside_issues.dto.EmailMessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(EmailMessageDTO emailMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailMessage.getToEmail());
            mimeMessageHelper.setText(emailMessage.getContent());
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
