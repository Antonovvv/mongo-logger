package com.qww.mongologger.admin.entity;

public class QueryLogParam extends DBInfo {
    private String requestURL;
    private String requestMethod;
    private int pageSize = 20;
    private int current = 1;
//    private Map<String, Object[]> filter;
//    private Map<String, Object> sorter;

    QueryLogParam() {}

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "QueryLogParam{" +
                "requestURL='" + requestURL + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", pageSize=" + pageSize +
                ", current=" + current +
                "} " + super.toString();
    }
}
