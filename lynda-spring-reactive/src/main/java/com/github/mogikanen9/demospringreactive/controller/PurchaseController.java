package com.github.mogikanen9.demospringreactive.controller;

import java.time.LocalDateTime;

import com.github.mogikanen9.demospringreactive.model.Purchase;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/coin/purchase/v1")
public class PurchaseController {

    @PostMapping(value = "/{name}")
    public Mono<Purchase> createPurchase(@PathVariable String name) {
        return Mono.fromSupplier(() -> new Purchase("1", "my name", "my price", LocalDateTime.now()));
    }
}