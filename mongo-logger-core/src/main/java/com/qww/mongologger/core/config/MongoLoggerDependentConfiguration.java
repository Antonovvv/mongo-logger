package com.qww.mongologger.core.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoLoggerDependentConfiguration {
    @Bean
    MongoDatabaseFactory _mongoDatabaseFactory(MongoLoggerOptionProperties mongoLoggerOptionProperties) throws Exception {
        MongoClient mongoClient;
        if (mongoLoggerOptionProperties.getUri() != null) {
            String mongoURI = mongoLoggerOptionProperties.getUri();
            ConnectionString connectionString = new ConnectionString(mongoURI);
            String database = connectionString.getDatabase();
            if (database == null) throw new IllegalArgumentException("Missing Database name in uri");
            mongoClient = MongoClients.create(mongoURI);
            return new SimpleMongoClientDatabaseFactory(mongoClient, database);
        } else {
            MongoClientSettingsFactory mongoClientSettingsFactory = new MongoClientSettingsFactory();
            if (mongoLoggerOptionProperties.getReplicaSetName() != null)
                mongoClientSettingsFactory.setClusterRequiredReplicaSetName(mongoLoggerOptionProperties.getReplicaSetName());
            MongoClientSettings mongoClientSettings = mongoClientSettingsFactory.createInstance();

            MongoClientFactory mongoClientFactory = new MongoClientFactory();
            mongoClientFactory.setMongoClientSettings(mongoClientSettings);
            mongoClientFactory.setHost(mongoLoggerOptionProperties.getHost());
            mongoClientFactory.setPort(mongoLoggerOptionProperties.getPort());
            List<MongoCredential> mongoCredentialList = new ArrayList<>();
            if (mongoLoggerOptionProperties.getUsername() != null) {
                mongoCredentialList.add(MongoCredential.createScramSha1Credential(
                        mongoLoggerOptionProperties.getUsername(),
                        mongoLoggerOptionProperties.getAuthenticationDatabase() != null ?
                                mongoLoggerOptionProperties.getAuthenticationDatabase() :
                                mongoLoggerOptionProperties.getDatabase(),
                        mongoLoggerOptionProperties.getPassword()
                ));
            }
            MongoCredential[] mongoCredentials = new MongoCredential[mongoCredentialList.size()];
            mongoClientFactory.setCredential(mongoCredentialList.toArray(mongoCredentials));
            mongoClient = mongoClientFactory.createInstance();
            return new SimpleMongoClientDatabaseFactory(mongoClient, mongoLoggerOptionProperties.getDatabase());
        }
    }

    @Bean
    MongoOperations _mongoTemplate(MongoLoggerOptionProperties mongoLoggerOptionProperties) throws Exception {
        return new MongoTemplate(_mongoDatabaseFactory(mongoLoggerOptionProperties));
    }
}
