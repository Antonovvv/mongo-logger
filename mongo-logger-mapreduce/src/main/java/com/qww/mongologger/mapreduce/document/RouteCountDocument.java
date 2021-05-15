package com.qww.mongologger.mapreduce.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class RouteCountDocument {
    @Id
    private RouteInfo route;
    @Field("value")
    private int count;

    public RouteInfo getRoute() {
        return route;
    }

    public int getCount() {
        return count;
    }
}

class RouteInfo {
    private String route;
    private String method;

    public String getRoute() {
        return route;
    }

    public String getMethod() {
        return method;
    }
}
