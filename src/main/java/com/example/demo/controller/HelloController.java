package com.example.demo.controller;

import com.example.demo.annotation.OperateLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    @OperateLog(operateModule = "hello", operateType = "测试", operateDesc = "说hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @GetMapping("/hello2")
    @OperateLog(operateModule = "hello2", operateType = "测试2", operateDesc = "说hello2")
    public String hello2(String name) {
        return name+ "Hello Spring Boot!";
    }

    @GetMapping("/hello3")
    @OperateLog()
    public String hello3(int a) {
        int i = 1/a;
        return "Hello Spring Boot!" + i;
    }
}
