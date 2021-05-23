package com.qww.mongologger.mapreduce.routetimeline;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import com.qww.mongologger.mapreduce.RouteKey;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DefaultStringifier;
import org.apache.hadoop.mapreduce.Job;
import org.bson.Document;

import java.io.IOException;
import java.util.Date;

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

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        String inputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.param?authSource=admin";
        String outputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.routeTimeline?authSource=admin";

        Date startTime = null; Date endTime = null;

        MongoCollection<Document> inputCollection = MongoDBUtil.getMongoCollection(inputURI);
        for (Document document : inputCollection.find().sort(new BasicDBObject("logTime", 1)).limit(1)) {
            startTime = document.getDate("logTime");
        }
        for (Document document : inputCollection.find().sort(new BasicDBObject("logTime", -1)).limit(1)) {
            endTime = new Date((document.getDate("logTime").getTime()) + 1000);
        }
        TimelineHelper timelineHelper = new TimelineHelper(startTime, endTime);

        boolean result = RouteTimeline.run(inputURI, outputURI, timelineHelper);
        System.exit(result ? 0 : 1);
    }
}
