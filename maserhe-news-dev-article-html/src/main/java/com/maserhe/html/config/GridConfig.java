package com.maserhe.html.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridConfig {

    @Value("${spring.data.mongodb.database}")
    private String mongodb;


    @Bean
    GridFSBucket gridFSBucket(MongoClient client) {
        MongoDatabase mongoDatabase = client.getDatabase(mongodb);
        GridFSBucket buckets = GridFSBuckets.create(mongoDatabase);
        return buckets;
    }
}