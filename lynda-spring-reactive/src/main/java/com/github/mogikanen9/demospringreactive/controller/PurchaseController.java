package com.github.mogikanen9.demospringreactive.controller;

import com.github.mogikanen9.demospringreactive.model.Purchase;
import com.github.mogikanen9.demospringreactive.service.CoinbaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/coin/purchase/v1")
public class PurchaseController {

    @Autowired
    private CoinbaseService coinbaseService;

    @PostMapping(value = "/{name}")
    public Mono<Purchase> createPurchase(@PathVariable("name") String name) {
        return coinbaseService.createPurchase(name);
    }

}