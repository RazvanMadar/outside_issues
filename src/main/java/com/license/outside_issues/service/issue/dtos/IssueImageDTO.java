package com.license.outside_issues.service.issue.dtos;

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
