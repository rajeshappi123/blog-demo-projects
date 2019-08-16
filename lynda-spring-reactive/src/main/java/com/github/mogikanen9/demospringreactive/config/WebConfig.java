package com.github.mogikanen9.demospringreactive.config;

import com.github.mogikanen9.demospringreactive.controller.PurchaseHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class WebConfig {

    @Bean
    public PurchaseHandler purchaseHandler() {
        return new PurchaseHandler();
    }
}
