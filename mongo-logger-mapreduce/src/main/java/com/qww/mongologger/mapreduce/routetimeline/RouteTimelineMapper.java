package com.qww.mongologger.mapreduce.routetimeline;

import com.qww.mongologger.mapreduce.RouteKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.Date;

public class RouteTimelineMapper extends Mapper<Object, BSONObject, RouteTimelineKey, RouteKey> {
    // private final static IntWritable one = new IntWritable(1);
    private final RouteTimelineKey routeTimelineKey = new RouteTimelineKey();
    private final RouteKey routeKey = new RouteKey();

    @Override
    protected void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        // super.map(key, value, context);
        String requestURL = (String) value.get("requestURL");
        String requestMethod = (String) value.get("requestMethod");
        Date logTime = (Date) value.get("logTime");

        if (!requestURL.equals("") && !requestMethod.equals("")) {
            if (routeTimelineKey.isNeedTimelineHelper) {
                Configuration conf = context.getConfiguration();
                TimelineHelper timelineHelper = DefaultStringifier.load(conf, "timelineHelper", TimelineHelper.class);
                routeTimelineKey.setTimeLineHelper(timelineHelper);
            }
            routeTimelineKey.set(requestURL, requestMethod, logTime);
            routeKey.set(requestURL, requestMethod);
            context.write(routeTimelineKey, routeKey);
        }
    }
}
