package com.license.outside_issues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.license.outside_issues.enums.ImageType;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
@Table(name = "issue_images")
public class IssueImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    private String type;
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] image;
    @Column(name = "number")
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="issue_id", nullable = false)
    private Issue issue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }
}
