package com.example.springwiki.controller;

import com.example.springwiki.domain.Test;
import com.example.springwiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private TestService testService;
    @Value("${hello.test:T}")
    private String test;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello world" + test;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello world" + name;
    }

    @GetMapping("/list")
    public List<Test> list() {
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key:{},value:{}", key, value);
        return "success";
    }

    @RequestMapping("redis/get/{key}")
    public Object get(@PathVariable String key) {
        Object o = redisTemplate.opsForValue().get(key);
        LOG.info("key:{},value:{}", key, o);
        return o;
    }

}
