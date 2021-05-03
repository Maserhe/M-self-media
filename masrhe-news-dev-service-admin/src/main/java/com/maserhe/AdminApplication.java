package com.maserhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 15:07
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.n3r.idworker", "com.maserhe"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}