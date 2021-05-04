package com.maserhe.file.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 21:53
 */
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
