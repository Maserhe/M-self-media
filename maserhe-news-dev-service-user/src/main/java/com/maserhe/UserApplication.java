package com.maserhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:24
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.n3r.idworker", "com.maserhe"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
