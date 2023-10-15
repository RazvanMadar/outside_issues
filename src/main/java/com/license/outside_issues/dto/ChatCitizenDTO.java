package com.license.outside_issues.dto;

public class ChatCitizenDTO {
    private Long citizenId;
    private String firstName;
    private String lastName;
    private String email;
    private byte[] image;

    public ChatCitizenDTO() {}

    public ChatCitizenDTO(Long citizenId, String firstName, String lastName, String email, byte[] image) {
        this.citizenId = citizenId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
