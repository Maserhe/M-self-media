package com.maserhe.article.controller;

import com.maserhe.api.controller.user.HelloControllerApi;
import com.maserhe.api.config.RabbitMQ;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 20:49
 */
@RestController
public class HelloController implements HelloControllerApi {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 匹配规则 article.*
     * @return
     */
    @Override
    public String hello() {

        rabbitTemplate.convertAndSend(RabbitMQ.EXCHANGE_ARTICLE,
                "article.hello",
                "这是从生产者发送的消息");

        return "123456";
    }
}
