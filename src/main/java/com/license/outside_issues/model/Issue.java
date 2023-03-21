package com.license.outside_issues.model;

import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private IssueType type;

    // astea 5 ar putea lipsi daca as face un DTO pt creare issue
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private IssueState state;
    private LocalDate reportedDate;
    private Integer likesNumber;
    private Integer dislikesNumber;
    //

    private String description;
    private Boolean hasLocation;
    private String actualLocation;
    private String citizenEmail;

    @OneToMany(
            mappedBy = "issue",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    private Set<IssueImage> images;
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "citizen_id", nullable = false)
//    private Citizen citizen;

    @OneToMany(
            mappedBy = "issue",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    Set<CitizenReactions> citizenReactions;

    public Issue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public Integer getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(Integer likesNumber) {
        this.likesNumber = likesNumber;
    }

    public Integer getDislikesNumber() {
        return dislikesNumber;
    }

    public void setDislikesNumber(Integer dislikesNumber) {
        this.dislikesNumber = dislikesNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCitizenEmail() {
        return citizenEmail;
    }

    public void setCitizenEmail(String citizenEmail) {
        this.citizenEmail = citizenEmail;
    }

    public Boolean getHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(Boolean hasLocation) {
        this.hasLocation = hasLocation;
    }

    public Set<IssueImage> getImages() {
        return images;
    }

    public void setImages(Set<IssueImage> images) {
        this.images = images;
    }

    public String getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(String actualLocation) {
        this.actualLocation = actualLocation;
    }

    public Set<CitizenReactions> getCitizenReactions() {
        return citizenReactions;
    }

    public void setCitizenReactions(Set<CitizenReactions> citizenReactions) {
        this.citizenReactions = citizenReactions;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", type=" + type +
                ", address=" + address +
                ", state=" + state +
                ", reportedDate=" + reportedDate +
                ", likesNumber=" + likesNumber +
                ", dislikesNumber=" + dislikesNumber +
                ", description='" + description + '\'' +
                ", hasLocation=" + hasLocation +
                ", actualLocation='" + actualLocation + '\'' +
                ", citizenEmail=" + citizenEmail +
                ", images=" + images +
                ", citizenReactions=" + citizenReactions +
                '}';
    }
}
