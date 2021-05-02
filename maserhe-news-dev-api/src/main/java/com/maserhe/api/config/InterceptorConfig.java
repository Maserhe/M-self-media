package com.maserhe.api.config;

import com.maserhe.api.interceptors.PassportInterceptor;
import com.maserhe.api.interceptors.UserActiveInterceptor;
import com.maserhe.api.interceptors.UserTokenInterceptor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }


    @Bean
    public UserTokenInterceptor userTokenInterceptor(){
        return new UserTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor userActiveInterceptor() {
        return new UserActiveInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor()).addPathPatterns("/passport/getSMSCode");
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/user/getAccountInfo");
        // registry.addInterceptor(userActiveInterceptor())
        //        .addPathPatterns("/user/getAccountInfo");
    }


}
