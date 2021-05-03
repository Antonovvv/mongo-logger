package com.qww.mongologger.mapreduce.routecount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;

public class RouteCountMapper extends Mapper<Object, BSONObject, RouteCountKey, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private final RouteCountKey routeCountKey = new RouteCountKey();

    @Override
    protected void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String requestURL = (String) value.get("requestURL");
        String requestMethod = (String) value.get("requestMethod");

        if (!requestURL.equals("") && !requestMethod.equals("")) {
            routeCountKey.set(requestURL, requestMethod);
            context.write(routeCountKey, one);
        }
    }
}
