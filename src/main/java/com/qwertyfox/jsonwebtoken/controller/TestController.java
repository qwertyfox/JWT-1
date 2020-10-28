package com.qwertyfox.jsonwebtoken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {




    @GetMapping
    public String getTestUrl() {
        System.out.println("Get mapping was called.");
        return "Test passed";
    }


}