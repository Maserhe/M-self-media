package com.maserhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 15:07
 */
@SpringBootApplication//(exclude = MongoAutoConfiguration.class)
@ComponentScan(basePackages = {"org.n3r.idworker", "com.maserhe"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}