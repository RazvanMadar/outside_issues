package com.license.outside_issues.dto;

import java.io.Serializable;

public class WebSocketMessageUpdateDTO implements Serializable {
    private static final long serialVersionUID = -1138446817700416884L;
    private String to;

    public WebSocketMessageUpdateDTO() {}

    public WebSocketMessageUpdateDTO(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
