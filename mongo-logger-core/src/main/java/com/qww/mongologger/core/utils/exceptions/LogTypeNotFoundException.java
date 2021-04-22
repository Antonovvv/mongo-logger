package com.qww.mongologger.core.utils.exceptions;

public class LogTypeNotFoundException extends MongoLoggerBaseException {
    @Override
    public String getMessage() {
        return "No LogType matching LogClass was found in Enum LogType";
    }
}
