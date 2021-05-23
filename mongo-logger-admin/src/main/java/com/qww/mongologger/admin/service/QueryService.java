package com.qww.mongologger.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoCollection;
import com.qww.mongologger.admin.entity.QueryLogParam;
import com.qww.mongologger.admin.utils.ServiceUtil;
import com.qww.mongologger.admin.utils.TableResult;
import com.qww.mongologger.core.entity.WebLog;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {
    public TableResult<WebLog> queryLog(QueryLogParam queryLogParam) throws JsonProcessingException {
        String address = queryLogParam.getAddress();
        String databaseName = queryLogParam.getDatabaseName();
        String collectionName = queryLogParam.getCollectionName();

        int pageSize = queryLogParam.getPageSize();
        int current = queryLogParam.getCurrent() - 1;
        List<WebLog> logList = new ArrayList<>();

        MongoCollection<Document> collection = ServiceUtil.getMongoCollection(address, databaseName, collectionName);
        long total = collection.count();
        for (Document document : collection.find().skip(current * pageSize).limit(pageSize)) {
            WebLog log = ServiceUtil.documentToPOJO(document, WebLog.class);
            logList.add(log);
        }
        return TableResult.success(logList, total);
    }
}
