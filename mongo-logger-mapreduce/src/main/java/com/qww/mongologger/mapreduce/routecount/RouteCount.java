package com.qww.mongologger.mapreduce.routecount;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

public class RouteCount {
    public static boolean run(String mongoInputURI, String mongoOutputURI) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("mongo.input.uri", mongoInputURI);
        conf.set("mongo.output.uri", mongoOutputURI);

        Job job = Job.getInstance(conf, "route count");
        job.setJarByClass(RouteCount.class);
        job.setMapperClass(RouteCountMapper.class);
        job.setReducerClass(RouteCountReducer.class);

        job.setMapOutputKeyClass(RouteKey.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(BSONWritable.class);
        job.setOutputValueClass(IntWritable.class);
        job.setSortComparatorClass(CountComparator.class);

        job.setInputFormatClass(MongoInputFormat.class);
        job.setOutputFormatClass(MongoOutputFormat.class);

        return job.waitForCompletion(true);
    }

    public static void main(String[] args) throws Exception {
        String inputURI;
        String outputURI;

        if (args.length == 0) {
            inputURI = "mongodb://122.51.139.75:27017/test.webLog";
            outputURI = "mongodb://122.51.139.75:27017/test.routeCount";
        } else if (args.length == 3) {
            String baseURI = args[0];
            String inputCollection = args[1];
            String outputCollection = args[2];
            inputURI = MongoDBUtil.getMongoURI(baseURI, inputCollection);
            outputURI = MongoDBUtil.getMongoURI(baseURI, outputCollection);
        } else {
            inputURI = "mongodb://122.51.139.75:27017/test.webLog";
            outputURI = "mongodb://122.51.139.75:27017/test.routeCount";
        }

        MongoDBUtil.cleanMongoCollection(outputURI);
        boolean result = RouteCount.run(inputURI, outputURI);
        System.exit(result ? 0 : 1);
    }
}
