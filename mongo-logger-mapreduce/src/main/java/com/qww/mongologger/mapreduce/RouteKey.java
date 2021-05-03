package com.qww.mongologger.mapreduce;

import java.util.Objects;

public class RouteKey {
    protected String requestURL;
    protected String requestMethod;

    public void set(String requestURL, String requestMethod) {
        this.setRequestURL(requestURL);
        this.setRequestMethod(requestMethod);
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return requestMethod;
    }


    @Override
    public String toString() {
        return "RouteKey{" +
                "requestURL='" + requestURL + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteKey)) return false;
        RouteKey routeKey = (RouteKey) o;
        return Objects.equals(getRequestURL(), routeKey.getRequestURL()) &&
                Objects.equals(getRequestMethod(), routeKey.getRequestMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequestURL(), getRequestMethod());
    }
}
