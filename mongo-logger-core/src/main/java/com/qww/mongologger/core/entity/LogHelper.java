package com.qww.mongologger.core.entity;

public class LogHelper<T extends BaseLog> {
    private T log;

    public void set(T log) {
        this.log = log;
    }

    public T get() {
        return this.log;
    }

    public Boolean isEmpty() {
        return this.log == null;
    }

    public void remove() {
        this.log = null;
    }
}
