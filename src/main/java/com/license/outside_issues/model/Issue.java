package com.license.outside_issues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    @Enumerated(EnumType.STRING)
    private IssueType type;

    // astea 5 ar putea lipsi daca as face un DTO pt creare issue
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private IssueState state;
    private LocalDate reportedDate;
    private Integer likesNumber;
    private Integer dislikesNumber;
    //

    private String description;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    public Issue() {}

    public Issue(Long id, String photo, IssueType type, Address address, IssueState state, LocalDate reportedDate, Integer likesNumber, Integer dislikesNumber, String description, Citizen citizen) {
        this.id = id;
        this.photo = photo;
        this.type = type;
        this.address = address;
        this.state = state;
        this.reportedDate = reportedDate;
        this.likesNumber = likesNumber;
        this.dislikesNumber = dislikesNumber;
        this.description = description;
        this.citizen = citizen;
    }

    public Issue(Long id, String photo, IssueType type, Double latitude, Double longitude, IssueState state, LocalDate reportedDate, Integer likesNumber, Integer dislikesNumber, String description, Citizen citizen) {
        this.id = id;
        this.photo = photo;
        this.type = type;
        this.address = new Address(latitude, longitude);
        this.state = state;
        this.reportedDate = reportedDate;
        this.likesNumber = likesNumber;
        this.dislikesNumber = dislikesNumber;
        this.description = description;
        this.citizen = citizen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Integer getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(Integer likesNumber) {
        this.likesNumber = likesNumber;
    }

    public Integer getDislikesNumber() {
        return dislikesNumber;
    }

    public void setDislikesNumber(Integer dislikesNumber) {
        this.dislikesNumber = dislikesNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}
