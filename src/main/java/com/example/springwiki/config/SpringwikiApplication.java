package com.example.springwiki.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("com.example")
@MapperScan("com.example.springwiki.mapper")
@EnableScheduling
@EnableAsync
public class SpringwikiApplication {
    private static final Logger LOG = LoggerFactory.getLogger(SpringwikiApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(SpringwikiApplication.class, args);
        SpringApplication app = new SpringApplication(SpringwikiApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址 http://localhost:{}", env.getProperty("server.port"));

    }
}
