package com.license.outside_issues.service.message.dtos;

import java.time.LocalDateTime;

public class MessageDTO {
    private String message;
    private String fromCitizen;
    private String toCitizen;
    private LocalDateTime date;

    public MessageDTO() {}

    public MessageDTO(String message, String fromCitizen, String toCitizen) {
        this.message = message;
        this.fromCitizen = fromCitizen;
        this.toCitizen = toCitizen;
    }

    public MessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromCitizen() {
        return fromCitizen;
    }

    public void setFromCitizen(String fromCitizen) {
        this.fromCitizen = fromCitizen;
    }

    public String getToCitizen() {
        return toCitizen;
    }

    public void setToCitizen(String toCitizen) {
        this.toCitizen = toCitizen;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "message='" + message + '\'' +
                ", fromCitizen='" + fromCitizen + '\'' +
                ", toCitizen='" + toCitizen + '\'' +
                ", date=" + date +
                '}';
    }
}
