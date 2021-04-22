package com.qww.mongologger.core.config;

import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.lang.NonNull;

public class MongoClientFactory extends MongoClientFactoryBean {
    @Override
    @NonNull
    protected MongoClient createInstance() throws Exception {
        return super.createInstance();
    }
}
