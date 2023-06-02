package com.license.outside_issues.dto;

import com.license.outside_issues.model.Citizen;

public class DisplayCitizenDTO {
    private Long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Integer totalReported;
    private Integer totalRejected;
    private boolean isBlocked;

    public DisplayCitizenDTO() {}

    public DisplayCitizenDTO(Citizen citizen) {
        this.id = citizen.getId();
        this.email = citizen.getEmail();
        this.firstName = citizen.getFirstName();
        this.lastName = citizen.getLastName();
        this.phoneNumber = citizen.getPhoneNumber();
    }

    public DisplayCitizenDTO(Long id, String email, String phoneNumber, String firstName, String lastName, Integer totalReported, Integer totalRejected, boolean isBlocked) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalReported = totalReported;
        this.totalRejected = totalRejected;
        this.isBlocked = isBlocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getTotalReported() {
        return totalReported;
    }

    public void setTotalReported(Integer totalReported) {
        this.totalReported = totalReported;
    }

    public Integer getTotalRejected() {
        return totalRejected;
    }

    public void setTotalRejected(Integer totalRejected) {
        this.totalRejected = totalRejected;
    }

    @Override
    public String toString() {
        return "DisplayCitizenDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", totalReported=" + totalReported +
                ", totalRejected=" + totalRejected +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
