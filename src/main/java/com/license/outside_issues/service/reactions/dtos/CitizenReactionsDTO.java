package com.license.outside_issues.service.reactions.dtos;

public class CitizenReactionsDTO {
    private Long citizenId;
    private Long issueId;
    private int type;

    public CitizenReactionsDTO() {}


    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CitizenReactionsDTO{" +
                "citizenId=" + citizenId +
                ", issueId=" + issueId +
                ", type=" + type +
                '}';
    }
}
