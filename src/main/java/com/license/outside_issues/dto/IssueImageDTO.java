package com.license.outside_issues.dto;

import com.license.outside_issues.model.CitizenImage;
import com.license.outside_issues.model.IssueImage;

public class IssueImageDTO {
    private Long id;
    private String type;
    private byte[] image;

    public IssueImageDTO() {}

    public IssueImageDTO(Long id, String type, byte[] image) {
        this.id = id;
        this.type = type;
        this.image = image;
    }

    public IssueImageDTO(IssueImage issue) {
        this.id = issue.getId();
        this.type = issue.getType();
        this.image = issue.getImage();
    }

    public IssueImageDTO(CitizenImage citizenImage) {
        this.id = citizenImage.getId();
        this.type = citizenImage.getType();
        this.image = citizenImage.getImage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
