package com.github.mogikanen9.demospringreactive.service;

import com.github.mogikanen9.demospringreactive.model.CoinBaseResponse;
import com.github.mogikanen9.demospringreactive.model.Purchase;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CoinbaseService {

    Mono<CoinBaseResponse> getCryptoPrice(String priceName);

    Mono<Purchase> createPurchase(String priceName);

    Mono<Purchase> getPurchaseById(String id);

    Flux<Purchase> listAllPurchases();

}
