package com.license.outside_issues.service.issue.dtos;

public class AddressDTO {
    private Double lat;
    private Double lng;

    public AddressDTO() {}

    public AddressDTO(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
