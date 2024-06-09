//package com.example.springwiki.config;
//
//import com.example.springwiki.interceptor.LogInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.annotation.Resource;
//
///**
// * @author jessica~
// * @version 1.0
// */
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//    @Resource
//    LogInterceptor logInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**")//对所有的请求
//                .excludePathPatterns("/login") // login不需要校验是否登录,就会陷入死循环
//        ;
//    }
//}
