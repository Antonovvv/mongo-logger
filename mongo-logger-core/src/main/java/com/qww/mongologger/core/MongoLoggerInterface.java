package com.qww.mongologger.core;

import com.qww.mongologger.core.entity.BaseLog;

public interface MongoLoggerInterface {
    /**
     * add a key-value record into log
     * @param key record key in log
     * @param value record value in log
     */
    void add(String key, Object value);

    /**
     * commit this log instance
     */
    void commit(BaseLog log);
    void commit(BaseLog log, String collectionName);
    void commit(String collectionName);
    void commit();

    /**
     * cleanup map after log commit
     */
    void cleanup();
}
