package com.license.outside_issues.dto;

import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.model.Issue;

import java.time.LocalDate;

public class IssueDTO {
    private Long id;
    private IssueType type;
    private AddressDTO address;
    private IssueState state;
    private LocalDate reportedDate;
    private Integer likesNumber;
    private Integer dislikesNumber;
    private String description;
    private Boolean hasLocation;
    private String citizenEmail;
    private String actualLocation;

    public IssueDTO() {}

    public IssueDTO(Issue issue) {
        this.id = issue.getId();
        this.type = issue.getType();
        this.state = issue.getState();
        this.reportedDate = issue.getReportedDate();
        this.likesNumber = issue.getLikesNumber();
        this.dislikesNumber = issue.getDislikesNumber();
        this.description = issue.getDescription();
        this.citizenEmail = issue.getCitizenEmail();
        this.address = new AddressDTO(issue.getAddress());
        this.hasLocation = issue.getHasLocation();
        this.actualLocation = issue.getActualLocation();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
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

    public String getCitizenEmail() {
        return citizenEmail;
    }

    public void setCitizenEmail(String citizenEmail) {
        this.citizenEmail = citizenEmail;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO addressDTO) {
        this.address = addressDTO;
    }

    public Boolean getHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(Boolean hasLocation) {
        this.hasLocation = hasLocation;
    }

    public String getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(String actualLocation) {
        this.actualLocation = actualLocation;
    }
}
