package com.qww.mongologger.admin.utils;

import java.util.List;

public class TableResult<T> {
    private List<T> data;
    private boolean success;
    private long total;

    public TableResult() {}

    public TableResult(List<T> data, boolean success, long total) {
        this.data = data;
        this.success = success;
        this.total = total;
    }

    public static <E> TableResult<E> success(List<E> data, long total) {
        return new TableResult<E>(data, true, total);
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public boolean getSuccess() {
        return success;
    }

    public long getTotal() {
        return total;
    }
}
