package com.license.outside_issues.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender mailSender;
    @Value("${base_url.issue}")
    private String issueURL;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            URL url = new URL(issueURL + emailMessage.getIssueId());
//            content.append("Sesizarea făcută de dumneavoastră de tipul ").append(type).append(" a fost înregistrată cu succes").append(state)
//                    .append(".\nPentru a vedea sesizarea, accesați linkul: ").append(url)
//                    .append(".\nVă mulțumim pentru contribuția dumneavoastră la menținerea si dezvoltarea orașului! ");
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailMessage.getToEmail());
            mimeMessageHelper.setText(emailMessage.getContent() + ".\n\nPentru a vedea sesizarea, accesați linkul: " + url);
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            mailSender.send(mimeMessage);
        } catch (MessagingException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
