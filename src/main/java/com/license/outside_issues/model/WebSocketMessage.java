package com.license.outside_issues.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WebSocketMessage implements Serializable {
    private static final long serialVersionUID = -1138446817700416884L;
    private String message;
    private String fromEmail;
    private String toEmail;

    public WebSocketMessage() {
    }

    public WebSocketMessage(String message, String fromEmail, String toEmail) {
        this.message = message;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }


    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "message='" + message + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", toEmail='" + toEmail + '\'' +
                '}';
    }

}