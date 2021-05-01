package com.maserhe.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 描述:  配置跨域
 *
 * @author Maserhe
 * @create 2021-05-01 19:24
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter getCorsFilter() {
        //1， 添加cors的配置信息
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        // 设置允许的header
        config.addAllowedHeader("*");
        // 设置是否发送cookie
        config.setAllowCredentials(true);

        config.addAllowedMethod("*");
        //2， 为url添加 映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }
}
