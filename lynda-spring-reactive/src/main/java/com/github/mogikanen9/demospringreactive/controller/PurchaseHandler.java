package com.github.mogikanen9.demospringreactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import com.github.mogikanen9.demospringreactive.model.Purchase;

public class PurchaseHandler {

    public Mono<ServerResponse> listPurchases(ServerRequest serverRequest) {
        final Mono<Purchase> purchase = Mono
                .fromSupplier(() -> new Purchase("123","From Functional Endpoint", "123", LocalDateTime.now()));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(purchase, Purchase.class);
    }
}
