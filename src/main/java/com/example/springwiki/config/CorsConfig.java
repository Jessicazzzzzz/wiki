package com.example.springwiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jessica~
 * @version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").// 针对所有的接口
                allowedOriginPatterns("*").
                allowedHeaders(CorsConfiguration.ALL).
                allowedMethods(CorsConfiguration.ALL).
                allowCredentials(true).maxAge(3600);// 一小时内不要再预检(发options请求)
    }
}
