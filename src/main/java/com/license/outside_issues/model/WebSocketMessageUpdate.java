package com.license.outside_issues.model;

import java.io.Serializable;

public class WebSocketMessageUpdate implements Serializable {
    private static final long serialVersionUID = -1138446817700416884L;
    private String to;

    public WebSocketMessageUpdate() {}

    public WebSocketMessageUpdate(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
