package com.qww.mongologger.mapreduce.document;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class RouteCountDocument {
    @Id
    @JsonAlias("_id")
    private RouteInfo routeInfo;
    @Field("value")
    @JsonAlias("value")
    private int count;

    public void setRouteInfo(RouteInfo routeInfo) {
        this.routeInfo = routeInfo;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RouteInfo getRouteInfo() {
        return routeInfo;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "RouteCountDocument{" +
                "routeInfo=" + routeInfo +
                ", count=" + count +
                '}';
    }
}

class RouteInfo {
    private String route;
    private String method;

    public void setRoute(String route) {
        this.route = route;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRoute() {
        return route;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "route='" + route + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
