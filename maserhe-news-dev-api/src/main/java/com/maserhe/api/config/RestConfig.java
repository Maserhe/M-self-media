package com.maserhe.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 17:23
 */
@Configuration
public class RestConfig {

    public RestConfig() {
    }

    /**
     * 会基于OKHttp3的配置来配置RestTemplate
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
