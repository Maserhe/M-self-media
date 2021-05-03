package com.maserhe.file.controller;

import com.maserhe.api.controller.user.HelloControllerApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-03 12:20
 */
@RestController
public class HelloController implements HelloControllerApi {

    @Override
    public String hello() {
        return "123456";
    }
}
