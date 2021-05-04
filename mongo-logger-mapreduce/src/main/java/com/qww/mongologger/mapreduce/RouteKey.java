package com.qww.mongologger.mapreduce;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class RouteKey implements Writable {
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

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.requestURL);
        dataOutput.writeUTF(this.requestMethod);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String reqURL = dataInput.readUTF();
        String reqMethod = dataInput.readUTF();
        this.set(reqURL, reqMethod);
    }
}
