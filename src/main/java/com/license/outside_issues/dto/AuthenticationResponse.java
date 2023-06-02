package com.license.outside_issues.dto;

public class AuthenticationResponse {
    private Long userId;
    private String email;
    private String accessToken;
    private String role;
    private String firstName;
    private String lastName;
    private boolean isBlocked;

    public AuthenticationResponse() { }

    public AuthenticationResponse(Long userId, String email, String role, String accessToken, String firstName, String lastName, boolean isBlocked) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isBlocked = isBlocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}

