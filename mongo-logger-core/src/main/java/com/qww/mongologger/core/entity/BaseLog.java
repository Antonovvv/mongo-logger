package com.qww.mongologger.core.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.qww.mongologger.core.annotation.LogType;
import com.qww.mongologger.core.entity.interfaces.BaseLogInterface;
import com.qww.mongologger.core.utils.exceptions.LogTypeNotFoundException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Document
public class BaseLog implements BaseLogInterface {
    @Id
    @JsonAlias("_id")
    private String id;
    private Date logTime;
    private Map<String, Object> payload = new HashMap<>();

    @Transient
    private LogType logType;

    public BaseLog() throws LogTypeNotFoundException {
        this.setLogType(LogType.getTypeByClass(this.getClass()));
        this.setLogTime(new Date());
    }

    /*
      logType私有，只能由构造方法赋值
     */
    private void setLogType(LogType logType) {
        this.logType = logType;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public void addPayload(String key, Object value) {
        payload.put(key, value);
    }

    public String getId() {
        return id;
    }

    public Date getLogTime() {
        return logTime;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "BaseLog{" +
                "id='" + id + '\'' +
                ", logTime=" + logTime +
                ", payload=" + payload +
                ", logType=" + logType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseLog)) return false;
        BaseLog baseLog = (BaseLog) o;
        return Objects.equals(logTime, baseLog.logTime) &&
                Objects.equals(payload, baseLog.payload) &&
                getLogType() == baseLog.getLogType();
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(logTime, payload, getLogType());
//    }
}
