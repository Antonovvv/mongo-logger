package com.qww.mongologger.core.entity;

import com.qww.mongologger.core.entity.interfaces.WebLogInterface;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Document
public class WebLog extends ExecLog implements WebLogInterface {
    private String requestURL;
    private String requestMethod;
    private String remoteAddr;
    private Integer remotePort;
    private String localAddr;
    private String localName;

    public WebLog() {}

//    public WebLog(HttpServletRequest request) {
//        this.setDataFromRequest(request);
//    }

    public void setDataFromRequest(HttpServletRequest request) {
        this.setRequestURL(request.getRequestURL().toString());
        this.setRequestMethod(request.getMethod());
        this.setRemoteAddr(request.getRemoteAddr());
        this.setRemotePort(request.getRemotePort());
        this.setLocalAddr(request.getLocalAddr());
        this.setLocalName(request.getLocalName());
    }

    public String getRequestURL() {
        return this.requestURL;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public Integer getRemotePort() {
        return this.remotePort;
    }

    public String getLocalAddr() {
        return this.localAddr;
    }

    public String getLocalName() {
        return this.localName;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public String toString() {
        return "WebLog{" +
                "requestURL='" + requestURL + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", remotePort=" + remotePort +
                ", localAddr='" + localAddr + '\'' +
                ", localName='" + localName + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WebLog)) return false;
        if (!super.equals(o)) return false;
        WebLog webLog = (WebLog) o;
        return Objects.equals(getRequestURL(), webLog.getRequestURL()) &&
                Objects.equals(getRequestMethod(), webLog.getRequestMethod()) &&
                Objects.equals(getRemoteAddr(), webLog.getRemoteAddr()) &&
                Objects.equals(getRemotePort(), webLog.getRemotePort()) &&
                Objects.equals(getLocalAddr(), webLog.getLocalAddr()) &&
                Objects.equals(getLocalName(), webLog.getLocalName());
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), getRequestURL(), getRequestMethod(), getRemoteAddr(), getRemotePort(), getLocalAddr(), getLocalName());
//    }
}
