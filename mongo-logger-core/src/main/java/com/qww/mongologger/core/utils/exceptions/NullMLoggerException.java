package com.qww.mongologger.core.utils.exceptions;

public class NullMLoggerException extends MongoLoggerBaseException {
    @Override
    public String getMessage() {
        return "MLogger field is declared but not assigned correctly";
    }
}
