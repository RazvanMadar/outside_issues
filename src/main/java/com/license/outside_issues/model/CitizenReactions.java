package com.license.outside_issues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "citizens_reactions")
public class CitizenReactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false, referencedColumnName = "id")
    private Citizen citizen;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false, referencedColumnName = "id")
    private Issue issue;
    @Column(name = "type")
    private Boolean type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CitizenReactions{" +
                "id=" + id +
                ", citizen=" + citizen +
                ", issue=" + issue +
                ", type=" + type +
                '}';
    }
}
