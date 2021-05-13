package com.maserhe.html.component;

import com.maserhe.api.config.RabbitMQ;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-12 22:19
 */
@Component
public class RabbitConsumer {


    @RabbitListener(queues = RabbitMQ.QUEUE_ARTICLE)
    public void watchQueue(String payload, Message message) {

        System.out.println(message.getMessageProperties().getReceivedRoutingKey());



    }
}
