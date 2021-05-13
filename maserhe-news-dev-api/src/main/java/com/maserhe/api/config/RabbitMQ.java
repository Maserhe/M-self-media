package com.maserhe.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-12 21:08
 */
@Configuration
public class RabbitMQ {

    // 定义交换机名称
    public static final String EXCHANGE_ARTICLE = "exchange_article";

    // 定义队列的名称
    public static final String QUEUE_ARTICLE = "queue_article";

    // 设置Bean的名字
    @Bean(EXCHANGE_ARTICLE)
    public Exchange exchange() {
        // 交换机
        return ExchangeBuilder.topicExchange(EXCHANGE_ARTICLE)
                // 持久化
                .durable(true)
                .build();
    }

    // 设置bean
    @Bean(QUEUE_ARTICLE)
    public Queue queue() {
        return new Queue(QUEUE_ARTICLE);
    }

    // 绑定交换机
    @Bean
    public Binding binding(@Qualifier(QUEUE_ARTICLE) Queue queue, @Qualifier(EXCHANGE_ARTICLE) Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("article.*") // 匹配路由规则
                .noargs();
    }

}
