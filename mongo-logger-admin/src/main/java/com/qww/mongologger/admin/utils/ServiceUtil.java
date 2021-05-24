package com.qww.mongologger.admin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.qww.mongologger.mapreduce.MongoDBUtil;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;

public class ServiceUtil {
    public static MongoCollection<Document> getMongoCollection(String address, String databaseName, String collectionName) {
        String mongoURL = "mongodb://mongologger:mongo-logger@"
                + address
                + "/" + databaseName
                + "." + collectionName
                + "?authSource=admin";
        return MongoDBUtil.getMongoCollection(mongoURL);
    }

    public static <T> T documentToPOJO(Document document, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonWriterSettings settings = JsonWriterSettings.builder()
                .objectIdConverter(new JSONObjectIdConverter())
                .dateTimeConverter(new JSONDateTimeConverter())
                .build();
        String json = document.toJson(settings);
        return objectMapper.readValue(json, clazz);
    }
}
