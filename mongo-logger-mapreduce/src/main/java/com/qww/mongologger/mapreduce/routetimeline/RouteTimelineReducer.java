package com.qww.mongologger.mapreduce.routetimeline;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;
import com.qww.mongologger.mapreduce.RouteKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RouteTimelineReducer extends Reducer<RouteTimelineKey, RouteKey, BSONWritable, BSONWritable> {
    private final BSONWritable outputKey = new BSONWritable();
    private final BSONWritable outputValue = new BSONWritable();

    @Override
    protected void reduce(RouteTimelineKey key, Iterable<RouteKey> values, Context context) throws IOException, InterruptedException {
        // super.reduce(key, values, context);
        Configuration conf = context.getConfiguration();
        TimelineHelper timelineHelper = DefaultStringifier.load(conf, "timelineHelper", TimelineHelper.class);
        int total = 0;
        Map<Map<String, String>, Integer> map = new HashMap<>();
        for (RouteKey value : values) {
            Map<String, String> routeKeyMap = new HashMap<>();
            routeKeyMap.put("route", value.getRequestURL());
            routeKeyMap.put("method", value.getRequestMethod());
            if (!map.containsKey(routeKeyMap)) {
                map.put(routeKeyMap, 1);
            } else {
                int count = map.get(routeKeyMap);
                map.replace(routeKeyMap, count + 1);
            }
            total += 1;
        }
        try {
            BSONObject keyBSON = BasicDBObjectBuilder.start()
                    .add("time", timelineHelper.getTimeInLine(key.getLogTime()))
                    .get();
            BSONObject valBSON = new BasicBSONObject();
            BasicBSONList routeList = new BasicBSONList();

            for (Map.Entry<Map<String, String>, Integer> route : map.entrySet()) {
                Map<String, String> routeMap = route.getKey();
                int count = route.getValue();
                BSONObject routeBSON = new BasicBSONObject();
                routeBSON.putAll(routeMap);
                routeBSON.put("count", count);
                routeList.add(routeBSON);
            }
            valBSON.put("routes", routeList);
            valBSON.put("total", total);

            outputKey.setDoc(keyBSON);
            outputValue.setDoc(valBSON);
            context.write(outputKey, outputValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
