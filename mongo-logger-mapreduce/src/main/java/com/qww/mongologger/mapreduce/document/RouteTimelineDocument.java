package com.qww.mongologger.mapreduce.document;

import com.qww.mongologger.mapreduce.routetimeline.TimelineHelper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.ParseException;
import java.util.Date;

@Document
public class RouteTimelineDocument {
    @Id
    private LogTime logTime;
    @Field("routes")
    private RouteTimelineInfo[] routes;
    private int total;

    public LogTime getLogTime() {
        return logTime;
    }

    public RouteTimelineInfo[] getRoutes() {
        return routes;
    }

    public int getTotal() {
        return total;
    }
}

class LogTime {
    private String time;

    public String getTime() {
        return time;
    }

    public Date getDateTime() throws ParseException {
        return TimelineHelper.sdf.parse(time);
    }
}

class RouteTimelineInfo extends RouteInfo {
    private int count;

    public int getCount() {
        return count;
    }
}
