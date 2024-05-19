package com.example.springwiki.controller;

import com.example.springwiki.domain.Test;
import com.example.springwiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
public class TestController {
    @Resource
    private TestService testService;
    @Value("${hello.test:T}")
    private  String test;
    @RequestMapping("/hello")
    public  String hello(){
        return "Hello world"+test;
    }
    @PostMapping("/hello/post")
    public  String helloPost(String name){
        return "Hello world"+name;
    }
    @GetMapping("/list")
    public List<Test> list(){
        return  testService.list();
    }


}
