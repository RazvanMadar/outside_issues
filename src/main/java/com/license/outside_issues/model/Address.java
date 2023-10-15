package com.license.outside_issues.model;

import com.license.outside_issues.dto.AddressDTO;

import javax.persistence.Embeddable;


@Embeddable
public class Address {
    private Double lat;
    private Double lng;

    public Address() {}

    public Address(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Address(AddressDTO addressDTO) {
        this.lat = addressDTO.getLat();
        this.lng = addressDTO.getLng();
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

    @Override
    public String toString() {
        return "Address{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
