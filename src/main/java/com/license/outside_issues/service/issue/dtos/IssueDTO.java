package com.license.outside_issues.service.issue.dtos;

import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;

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
    private Long citizenId;

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

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
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
}
