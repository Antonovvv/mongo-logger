package com.qww.mongologger.core.entity.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface BaseLogInterface {
    public void addPayload(String key, Object value) throws JsonProcessingException;
}
