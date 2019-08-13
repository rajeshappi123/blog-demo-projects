package com.github.mogikanen9.demospringreactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/coin/price/v1")
public class PriceController {

    @GetMapping(value = "/{name}")
    public Mono<String> getPrice(@PathVariable String name) {
        return Mono.fromSupplier(() -> "price");
    }
}