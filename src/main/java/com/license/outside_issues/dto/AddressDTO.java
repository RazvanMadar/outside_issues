package com.license.outside_issues.dto;

import com.license.outside_issues.model.Address;

public class AddressDTO {
    private Double lat;
    private Double lng;

    public AddressDTO() {}

    public AddressDTO(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public AddressDTO(Address address) {
        this.lat = address.getLat();
        this.lng = address.getLng();
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
