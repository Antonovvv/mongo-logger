package com.qww.mongologger.mapreduce.document;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.qww.mongologger.mapreduce.routetimeline.TimelineHelper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.ParseException;
import java.util.Date;

@Document
public class RouteTimelineDocument {
    @Id
    @JsonAlias("_id")
    private LogTime logTime;
    @Field("routes")
    @JsonAlias("routes")
    private RouteTimelineInfo[] routes;
    private int total;

    public void setLogTime(LogTime logTime) {
        this.logTime = logTime;
    }

    public void setRoutes(RouteTimelineInfo[] routes) {
        this.routes = routes;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public RouteTimelineInfo[] getRoutes() {
        return routes;
    }

    public LogTime getLogTime() {
        return logTime;
    }

    public int getTotal() {
        return total;
    }
}

class LogTime {
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public Date getDateTime() throws ParseException {
        return TimelineHelper.sdf.parse(time);
    }

    public long getTimestamp() throws ParseException {
        return TimelineHelper.sdf.parse(time).getTime();
    }
}

class RouteTimelineInfo extends RouteInfo {
    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
