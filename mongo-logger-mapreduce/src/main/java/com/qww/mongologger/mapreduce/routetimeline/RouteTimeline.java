package com.qww.mongologger.mapreduce.routetimeline;

import com.mongodb.ConnectionString;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import com.qww.mongologger.mapreduce.RouteKey;
import com.qww.mongologger.mapreduce.routecount.CountComparator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

public class RouteTimeline {
    public static boolean run(String mongoInputURI, String mongoOutputURI, TimelineHelper timelineHelper) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("mongo.input.uri", mongoInputURI);
        conf.set("mongo.output.uri", mongoOutputURI);
        DefaultStringifier.store(conf, timelineHelper, "timelineHelper");

        Job job = Job.getInstance(conf, "route timeline");
        job.setJarByClass(RouteTimeline.class);
        job.setMapperClass(RouteTimelineMapper.class);
        job.setReducerClass(RouteTimelineReducer.class);

        job.setMapOutputKeyClass(RouteTimelineKey.class);
        job.setMapOutputValueClass(RouteKey.class);
        job.setOutputKeyClass(BSONWritable.class);
        job.setOutputValueClass(BSONWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        job.setOutputFormatClass(MongoOutputFormat.class);

        MongoDBUtil.cleanMongoCollection(new ConnectionString(mongoOutputURI));
        return job.waitForCompletion(true);
    }
}
