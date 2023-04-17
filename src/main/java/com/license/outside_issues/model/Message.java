package com.license.outside_issues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "from_citizen_id", nullable = false, referencedColumnName = "id")
    private Citizen fromCitizen;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "to_citizen_id", nullable = false, referencedColumnName = "id")
    private Citizen toCitizen;
    private LocalDateTime date;

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Citizen getFromCitizen() {
        return fromCitizen;
    }

    public void setFromCitizen(Citizen fromCitizen) {
        this.fromCitizen = fromCitizen;
    }

    public Citizen getToCitizen() {
        return toCitizen;
    }

    public void setToCitizen(Citizen toCitizen) {
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
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", fromCitizen=" + fromCitizen +
                ", toCitizen=" + toCitizen +
                ", date=" + date +
                '}';
    }
}
