package com.license.outside_issues.service.citizen.dtos;

import java.util.Arrays;

public class CitizenImageDTO {
    private Long id;
    private byte[] image;

    public CitizenImageDTO() {}

    public CitizenImageDTO(Long id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CitizenImage{" +
                "id=" + id +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
