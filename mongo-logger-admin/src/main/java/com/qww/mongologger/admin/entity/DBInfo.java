package com.qww.mongologger.admin.entity;

public class DBInfo {
    private String address;
    private String databaseName;
    private String collectionName;

    DBInfo() {}

    public DBInfo(String address, String databaseName, String collectionName) {
        this.address = address;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getAddress() {
        return address;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public String toString() {
        return "DBInfo{" +
                "address='" + address + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", collectionName='" + collectionName + '\'' +
                '}';
    }
}

