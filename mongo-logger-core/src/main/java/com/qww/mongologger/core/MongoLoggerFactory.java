package com.qww.mongologger.core;

import com.qww.mongologger.core.utils.SpringBeanUtil;

public class MongoLoggerFactory {
    public static MongoLogger getMongoLogger() {
        return SpringBeanUtil.getBean(MongoLogger.class);
    }
}
