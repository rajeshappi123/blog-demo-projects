package com.github.mogikanen9.demo.rest.api.hello;

import com.google.common.util.concurrent.RateLimiter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloControllerGuava {

    final RateLimiter rateLimiter = RateLimiter.create(5);

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {

        //emulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!rateLimiter.tryAcquire()){
           return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
        
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }
}