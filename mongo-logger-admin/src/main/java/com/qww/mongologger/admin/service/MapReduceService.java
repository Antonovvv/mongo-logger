package com.qww.mongologger.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.qww.mongologger.admin.utils.ServiceUtil;
import com.qww.mongologger.admin.utils.TableResult;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import com.qww.mongologger.mapreduce.document.RouteCountDocument;
import com.qww.mongologger.mapreduce.document.RouteTimelineDocument;
import com.qww.mongologger.mapreduce.routecount.RouteCount;
import com.qww.mongologger.mapreduce.routetimeline.RouteTimeline;
import com.qww.mongologger.mapreduce.routetimeline.TimelineHelper;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MapReduceService {
    public boolean runRouteCount() throws InterruptedException, IOException, ClassNotFoundException {
        String inputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.param?authSource=admin";
        String outputURI = "mongodb://mongologger:mongo-logger@122.51.139.75:27777/test.routeCount?authSource=admin";

//        ConnectionString outputConnectionString = new ConnectionString(outputURI);
//        MongoClient client = MongoClients.create(outputURI);
//        MongoDatabase database = client.getDatabase(outputConnectionString.getDatabase());
//        MongoCollection<?> collection = database.getCollection(outputConnectionString.getCollection());
//        collection.drop();
        return RouteCount.run(inputURI, outputURI);
    }

    public boolean runRouteTimeline() throws InterruptedException, IOException, ClassNotFoundException {
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

        return RouteTimeline.run(inputURI, outputURI, timelineHelper);
    }

    public TableResult<RouteCountDocument> getRouteCount() throws JsonProcessingException {
        List<RouteCountDocument> countList = new ArrayList<>();
        MongoCollection<Document> collection = ServiceUtil.getMongoCollection("122.51.139.75:27777", "test", "routeCount");
        long total = collection.count();
        for (Document document : collection.find()) {
            RouteCountDocument countRow = ServiceUtil.documentToPOJO(document, RouteCountDocument.class);
            countList.add(countRow);
        }
        return TableResult.success(countList, total);
    }

    public TableResult<RouteTimelineDocument> getRouteTimeline() throws JsonProcessingException {
        List<RouteTimelineDocument> timelineList = new ArrayList<>();
        MongoCollection<Document> collection = ServiceUtil.getMongoCollection("122.51.139.75:27777", "test", "routeTimeline");
        long total = collection.count();
        for (Document document : collection.find()) {
            RouteTimelineDocument timeline = ServiceUtil.documentToPOJO(document, RouteTimelineDocument.class);
            timelineList.add(timeline);
        }
        return TableResult.success(timelineList, total);
    }
}
