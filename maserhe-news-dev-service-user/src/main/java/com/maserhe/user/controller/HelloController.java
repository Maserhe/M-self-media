package com.maserhe.user.controller;

import com.maserhe.api.controller.user.HelloControllerApi;
import com.maserhe.sms.SendSMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.GregorianCalendar;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 17:14
 */
@RestController
public class HelloController implements HelloControllerApi {

    @Autowired
    private SendSMS sendSMS;

    public String hello(){

        logger.info("sdasdasd");
        return "Hello";
    }



}
