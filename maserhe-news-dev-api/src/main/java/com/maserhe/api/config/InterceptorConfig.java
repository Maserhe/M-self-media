package com.maserhe.api.config;

import com.maserhe.api.interceptors.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 19:50
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private PassportInterceptor passportInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).addPathPatterns("/passport/getSMSCode");
    }


}
