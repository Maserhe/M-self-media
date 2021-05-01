package com.maserhe.api.config;

import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 17:23
 */

public class RestConfig {

    public RestConfig() {
    }


    public RestTemplate getRestTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
