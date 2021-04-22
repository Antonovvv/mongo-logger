package com.qww.mongologger.core.entity.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ExecLogInterface extends BaseLogInterface {
    public void addMethodArg(String name, Class<?> type, Object value) throws JsonProcessingException;

    public void setMethodReturn(Class<?> type, Object value) throws JsonProcessingException;

    public void setMethodError(Throwable throwing);
}
