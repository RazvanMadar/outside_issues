package com.license.outside_issues.dto;

import com.sun.istack.NotNull;

public class AuthenticationRequestDTO {
    @NotNull
    private String email;

    @NotNull
    private String password;

    public AuthenticationRequestDTO() {

    }

    public AuthenticationRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

