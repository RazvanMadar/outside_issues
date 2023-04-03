package com.license.outside_issues.model;

import javax.persistence.*;

@Entity
@Table(name = "rejected_issues")
public class RejectedIssues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long citizenId;
    private Long rejectedNumber;

    public RejectedIssues() {}

    public RejectedIssues(Long id, Long citizenId, Long rejectedNumber) {
        this.id = id;
        this.citizenId = citizenId;
        this.rejectedNumber = rejectedNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getRejectedNumber() {
        return rejectedNumber;
    }

    public void setRejectedNumber(Long rejectedNumber) {
        this.rejectedNumber = rejectedNumber;
    }

    @Override
    public String toString() {
        return "RejectedIssues{" +
                "id=" + id +
                ", citizenId=" + citizenId +
                ", rejectedNumber=" + rejectedNumber +
                '}';
    }
}
