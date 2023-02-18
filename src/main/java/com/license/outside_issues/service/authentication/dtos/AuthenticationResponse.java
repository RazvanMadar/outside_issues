package com.license.outside_issues.service.authentication.dtos;

public class AuthenticationResponse {
    private Long userId;
    private String email;
    private String accessToken;
    private String role;

    public AuthenticationResponse() { }

    public AuthenticationResponse(Long userId, String email, String role, String accessToken) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
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
}

