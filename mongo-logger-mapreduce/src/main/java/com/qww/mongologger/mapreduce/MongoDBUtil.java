package com.qww.mongologger.mapreduce;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * mongo-hadoop依赖旧版mongo-java-driver,与mongodb-driver-core冲突（可能是org.bson包）
 * 这里使用旧版MongoClient避免冲突
 */
public class MongoDBUtil {
    /**
     * 返回完整格式的mongodb连接uri
     * @param baseURI eg. mongodb://host:port/database
     * @param collectionName 集合名称
     * @return eg. mongodb://host:port/database.collection
     */
    public static String getMongoURI(String baseURI, String collectionName) {
        return baseURI + "." + collectionName;
    }

    public static ConnectionString getMongoConnectionString(String baseURI, String collectionName) {
        return new ConnectionString(getMongoURI(baseURI, collectionName));
    }

    public static ConnectionString getMongoConnectionString(String mongoURI) {
        return new ConnectionString(mongoURI);
    }

    public static List<ServerAddress> getServerAddresses(ConnectionString connectionString) {
        List<ServerAddress> serverAddresses = new ArrayList<>();
        // 从connectionString获取的host包括port
        List<String> hosts = connectionString.getHosts();
        for (String host : hosts) {
            // ServerAddress会检测host中是否有:port
            serverAddresses.add(new ServerAddress(host));
        }
        return serverAddresses;
    }

    public static MongoClient getMongoClient(String mongoURI) {
        ConnectionString connectionString = getMongoConnectionString(mongoURI);
        return getMongoClient(connectionString);
    }

    public static MongoClient getMongoClient(ConnectionString connectionString) {
        List<ServerAddress> seeds = getServerAddresses(connectionString);
        if (connectionString.getUsername() != null) {
            List<MongoCredential> mongoCredentialList = connectionString.getCredentialList();
//            List<MongoCredential> mongoCredentialList = new ArrayList<>();
//            mongoCredentialList.add(MongoCredential.createScramSha1Credential(
//                    connectionString.getUsername(),
//                    connectionString.getDatabase(),
//                    connectionString.getPassword()
//            ));
            return new MongoClient(seeds, mongoCredentialList);
        } else {
            return new MongoClient(seeds);
        }
    }

    public static MongoCollection<Document> getMongoCollection(String mongoURI) {
        ConnectionString connectionString = getMongoConnectionString(mongoURI);
        return getMongoCollection(connectionString);
    }

    public static MongoCollection<Document> getMongoCollection(ConnectionString connectionString) {
        MongoClient client = getMongoClient(connectionString);
        return getMongoCollection(client, connectionString);
    }

    private static MongoCollection<Document> getMongoCollection(MongoClient client, ConnectionString connectionString) {
        String databaseName = connectionString.getDatabase();
        if (databaseName == null) throw new NullPointerException();
        MongoDatabase db = client.getDatabase(databaseName);
        String collectionName = connectionString.getCollection();
        if (collectionName == null) throw new NullPointerException();
        return db.getCollection(collectionName);
    }

    public static void cleanMongoCollection(String baseURI, String collectionName) {
        cleanMongoCollection(getMongoConnectionString(baseURI, collectionName));
    }

    public static void cleanMongoCollection(String mongoURI) {
        cleanMongoCollection(getMongoConnectionString(mongoURI));
    }

    public static void cleanMongoCollection(ConnectionString connectionString) {
        MongoClient client = getMongoClient(connectionString);
        MongoCollection<?> collection = getMongoCollection(client, connectionString);
        collection.drop();
        client.close();
    }
}
