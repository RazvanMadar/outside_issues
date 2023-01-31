package com.license.outside_issues.service.issue.dtos;

public class AddressDTO {
    private Double lat;
    private Double lng;
    private String adr;

    public AddressDTO() {}

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

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }
}
