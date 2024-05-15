package com.example.springwiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jessica~
 * @version 1.0
 */
@RestController
public class TestController {
    @RequestMapping("/hello")
    public  String hello(){
        return "Hello world";
    }
}
