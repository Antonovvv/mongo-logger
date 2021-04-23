package com.qww.mongologger.core;

import com.qww.mongologger.core.entity.BaseLog;
import com.qww.mongologger.core.utils.JSONUtil;
import com.qww.mongologger.core.utils.SpringBeanUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
// @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MongoLogger implements MongoLoggerInterface {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MongoLogger.class);
    private MongoTemplate mongoTemplate;
    Map<String, String> logMap = new HashMap<>();
    Map<String, String> mLogMap = new HashMap<>();
    boolean isInMLogAnnotation = false;

    public MongoLogger() {
        this.setMongoTemplate();
    }

    public void setMongoTemplate() {
        this.mongoTemplate = (MongoTemplate) SpringBeanUtil.getBean("_mongoTemplate");
    }
//    public void setMongoTemplate() {
//        this.mongoTemplate = SpringBeanUtil.getBean(MongoTemplate.class);
//    }

    @Override
    public void add(String key, Object value) {
        getProperMap().put(key, JSONUtil.stringify(value));
    }

    @Override
    public void commit(BaseLog log) {
        this.commit(log, null);
    }

    @Override
    public void commit(BaseLog log, String collectionName) {
        log.setInfo(getProperMap());
        if (collectionName == null || collectionName.equals("")) {
            mongoTemplate.save(log);
        } else {
            mongoTemplate.save(log, collectionName);
        }
        LOG.info("mongologger committed");
        this.cleanup();
    }

    @Override
    public void commit(String collectionName) {
        BaseLog log = new BaseLog();
        this.commit(log, collectionName);
    }

    @Override
    public void commit() {
        this.commit("");
    }

    @Override
    public void cleanup() {
        getProperMap().clear();
        this.setInMLogAnnotation(false);
    }

    public void setInMLogAnnotation(Boolean inMLogAnnotation) {
        isInMLogAnnotation = inMLogAnnotation;
    }

    private Map<String, String> getProperMap() {
        return this.isInMLogAnnotation ? mLogMap : logMap;
    }
}
