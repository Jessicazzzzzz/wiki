package com.example.springwiki.config;


import com.example.springwiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author jessica~
 * @version 1.0
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")//对所有的请求
                .excludePathPatterns("/test/**",
                        "/redis/**",
                        "/user/login",
                        "/category/all",
                        "/ebook/list",
                        "/doc/all/**",
                        "/doc/vote/**",
                        "/doc/find-content/**"
                ) // login不需要校验是否登录,就会陷入死循环
        ;
    }
}
