package com.github.mogikanen9.demospringreactive.controller;

import java.util.List;

import com.github.mogikanen9.demospringreactive.model.Purchase;
import com.github.mogikanen9.demospringreactive.service.CoinbaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PurchaseHandler {

@Autowired
    private CoinbaseService coinbaseService;

    public Mono<ServerResponse> listPurchases(ServerRequest serverRequest) {
        final Mono<Purchase> purchase =
                coinbaseService.getPurchaseById(serverRequest.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(purchase, Purchase.class);
    }

    public Mono<ServerResponse> listAllPurchases(ServerRequest serverRequest) {
        final Flux<Purchase> purchaseFlux = coinbaseService.listAllPurchases();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(purchaseFlux.collectList(), new ParameterizedTypeReference<List<Purchase>>() {});
    }
  
}
