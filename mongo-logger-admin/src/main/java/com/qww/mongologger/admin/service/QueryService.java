package com.qww.mongologger.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.qww.mongologger.admin.entity.QueryLogParam;
import com.qww.mongologger.admin.utils.JSONDateTimeConverter;
import com.qww.mongologger.admin.utils.JSONObjectIdConverter;
import com.qww.mongologger.admin.utils.TableResult;
import com.qww.mongologger.core.entity.WebLog;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {
    public TableResult<WebLog> queryLog(QueryLogParam queryLogParam) throws JsonProcessingException {
        String address = queryLogParam.getAddress();
        String databaseName = queryLogParam.getDatabaseName();
        String collectionName = queryLogParam.getCollectionName();
        String mongoURL = "mongodb://mongologger:mongo-logger@"
                + address
                + "/" + databaseName
                + "." + collectionName
                + "?authSource=admin";
        int pageSize = queryLogParam.getPageSize();
        int current = queryLogParam.getCurrent() - 1;
        List<WebLog> logList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonWriterSettings settings = JsonWriterSettings.builder()
                .objectIdConverter(new JSONObjectIdConverter())
                .dateTimeConverter(new JSONDateTimeConverter())
                .build();


        MongoCollection<Document> collection = MongoDBUtil.getMongoCollection(mongoURL);
        long total = collection.count();
        for (Document document : collection.find().skip(current * pageSize).limit(pageSize)) {
            String json = document.toJson(settings);
            WebLog log = objectMapper.readValue(json, WebLog.class);
            logList.add(log);
        }
        return TableResult.success(logList, total);
    }
}
