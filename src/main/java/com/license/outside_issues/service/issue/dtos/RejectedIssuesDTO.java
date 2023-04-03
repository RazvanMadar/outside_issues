package com.license.outside_issues.service.issue.dtos;

public class RejectedIssuesDTO {
    private String type;
    private Long val;

    public RejectedIssuesDTO() {
    }

    public RejectedIssuesDTO(String type, Long val) {
        this.type = type;
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getVal() {
        return val;
    }

    public void setVal(Long val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "RejectedIssuesDTO{" +
                "type='" + type + '\'' +
                ", val=" + val +
                '}';
    }
}
