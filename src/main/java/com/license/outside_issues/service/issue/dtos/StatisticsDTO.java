package com.license.outside_issues.service.issue.dtos;

public class StatisticsDTO {
    private String state;
    private Integer val;

    public StatisticsDTO() {}

    public StatisticsDTO(String state, Integer val) {
        this.state = state;
        this.val = val;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }
}
