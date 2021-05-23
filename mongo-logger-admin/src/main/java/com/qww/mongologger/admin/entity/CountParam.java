package com.qww.mongologger.admin.entity;

public class CountParam extends DBInfo {
    private int pageSize = 20;
    private int current = 1;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrent() {
        return current;
    }
}
