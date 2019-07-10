package com.github.mogikanen9.demo.rest.api.greetings.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController{

    @GetMapping("/simple/{name}")
    public String hi(@PathVariable String name){
        return String.format("Hello, %s!", name);
    }

}