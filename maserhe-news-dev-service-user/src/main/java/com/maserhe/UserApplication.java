package com.maserhe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:24
 */
@SpringBootApplication
@MapperScan(basePackages = "com.imooc.user.mapper")
@ComponentScan("com.maserhe")
public class UserApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
