package com.qww.mongologger.mapreduce.routecount;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import java.io.IOException;

public class RouteCountReducer extends Reducer<RouteCountKey, IntWritable, BSONWritable, IntWritable> {
    private final BSONWritable outPutKey = new BSONWritable();
    private final IntWritable outPutValue = new IntWritable();

    @Override
    protected void reduce(RouteCountKey key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // super.reduce(key, values, context);
        int sum = 0;
        for (final IntWritable value : values) {
            sum += value.get();
        }
        BSONObject bson = BasicDBObjectBuilder.start()
                .add("route", key.getRequestURL())
                .add("method", key.getRequestMethod())
                .get();
        outPutKey.setDoc(bson);
        outPutValue.set(sum);
        context.write(outPutKey, outPutValue);
    }
}
