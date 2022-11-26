package com.license.outside_issues.service.issue.dtos;

import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.model.Address;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IssueDTO {
    private Long id;
    private String photo;
    private IssueType type;
    private Double latitude;
    private Double longitude;
    private IssueState state;
    private LocalDate reportedDate;
    private Integer likesNumber;
    private Integer dislikesNumber;
    private String description;
    private Long citizenId;

    public IssueDTO() {}

    public IssueDTO(Long id, String photo, IssueType type, Double latitude, Double longitude, IssueState state, LocalDate reportedDate, Integer likesNumber, Integer dislikesNumber, String description, Long citizenId) {
        this.id = id;
        this.photo = photo;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.reportedDate = reportedDate;
        this.likesNumber = likesNumber;
        this.dislikesNumber = dislikesNumber;
        this.description = description;
        this.citizenId = citizenId;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
