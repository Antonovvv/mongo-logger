package com.qww.mongologger.mapreduce.routecount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.Date;

public class RouteCountMapper extends Mapper<Object, BSONObject, RouteKey, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private RouteKey routeKey = new RouteKey();

    @Override
    protected void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        String requestURL = (String) value.get("requestURL");
        String requestMethod = (String) value.get("requestMethod");

        if (!requestURL.equals("") && !requestMethod.equals("")) {
            routeKey.set(requestURL, requestMethod);
            context.write(routeKey, one);
        }
    }
}
