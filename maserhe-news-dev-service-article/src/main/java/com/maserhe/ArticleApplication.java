package com.maserhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述:
 *  文件的上传服务
 * @author Maserhe
 * @create 2021-05-04 20:47
 */
@SpringBootApplication
@ComponentScan(basePackages = {"org.n3r.idworker", "com.maserhe"})
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
