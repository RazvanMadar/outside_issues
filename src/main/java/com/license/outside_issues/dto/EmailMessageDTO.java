package com.license.outside_issues.dto;

public class EmailMessageDTO {
    private String subject;
    private String toEmail;
    private String content;
    private Long issueId;

    public EmailMessageDTO() {}

    public EmailMessageDTO(String subject, String toEmail, String content, Long issueId) {
        this.subject = subject;
        this.toEmail = toEmail;
        this.content = content;
        this.issueId = issueId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
