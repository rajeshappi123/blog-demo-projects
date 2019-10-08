package com.github.mogikanen9.spring.chat.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
public class AddController {

    private SimpMessagingTemplate template;

    @Autowired
    public AddController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @PostMapping("/mygreetings")
    public void greet(String greeting) {        
        this.template.convertAndSend("/topic/greetings", new Greeting(greeting));
    }
}