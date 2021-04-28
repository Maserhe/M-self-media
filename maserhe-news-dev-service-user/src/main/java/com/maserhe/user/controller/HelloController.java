package com.maserhe.user.controller;

import com.maserhe.api.controller.user.HelloControllerApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:14
 */
@RestController
public class HelloController implements HelloControllerApi {

    public String hello(){
        return "Hello";
    }

}
