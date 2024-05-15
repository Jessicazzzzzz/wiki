package com.example.springwiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
public class TestController {

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

}
