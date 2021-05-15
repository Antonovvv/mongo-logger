package com.qww.mongologger.admin.entity;

public class OriginalLog {
    private String requestURL;
    private String requestMethod;
    private String logTime;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getLogTime() {
        return logTime;
    }
}
