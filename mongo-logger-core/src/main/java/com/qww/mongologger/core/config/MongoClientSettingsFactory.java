package com.qww.mongologger.core.config;

import com.mongodb.MongoClientSettings;
import org.springframework.data.mongodb.core.MongoClientSettingsFactoryBean;
import org.springframework.lang.NonNull;

public class MongoClientSettingsFactory extends MongoClientSettingsFactoryBean {
    @Override
    @NonNull
    protected MongoClientSettings createInstance() {
        return super.createInstance();
    }
}
