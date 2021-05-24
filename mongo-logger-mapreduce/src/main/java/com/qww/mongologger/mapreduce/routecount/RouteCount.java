package com.qww.mongologger.mapreduce.routecount;

import com.mongodb.ConnectionString;
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
        // conf.set("fs.defaultFS", "hdfs://122.51.139.75:9000");
        conf.set("mongo.input.uri", mongoInputURI);
        conf.set("mongo.output.uri", mongoOutputURI);

        Job job = Job.getInstance(conf, "route count");
        job.setJarByClass(RouteCount.class);
        job.setMapperClass(RouteCountMapper.class);
        job.setReducerClass(RouteCountReducer.class);

        job.setMapOutputKeyClass(RouteCountKey.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(BSONWritable.class);
        job.setOutputValueClass(IntWritable.class);
        // job.setSortComparatorClass(CountComparator.class);

        job.setInputFormatClass(MongoInputFormat.class);
        job.setOutputFormatClass(MongoOutputFormat.class);

        MongoDBUtil.cleanMongoCollection(new ConnectionString(mongoOutputURI));
        return job.waitForCompletion(true);
    }

    public static void main(String[] args) throws Exception {
        String inputURI;
        String outputURI;
        String URI = "122.51.139.75:27777";
        String databaseName = "test";
        String collectionName = "example";

        if (args.length == 1) {
            collectionName = args[0];
        }
        inputURI = "mongodb://mongologger:mongo-logger@"+URI+"/"+databaseName+"."+collectionName+"?authSource=admin";
        outputURI = "mongodb://mongologger:mongo-logger@"+URI+"/"+databaseName+".routeCount?authSource=admin";

//        MongoDBUtil.cleanMongoCollection(outputURI);
        boolean result = RouteCount.run(inputURI, outputURI);
        System.exit(result ? 0 : 1);
    }
}
