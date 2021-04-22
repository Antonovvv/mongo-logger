package com.qww.mongologger.core.utils.exceptions;

public class MethodArgumentMismatchException extends MongoLoggerBaseException {
    @Override
    public String getMessage() {
        return "Method Arguments is mismatch to declared parameters";
    }
}
