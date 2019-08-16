package com.github.mogikanen9.demospringreactive.service;

import java.time.LocalDateTime;

import com.github.mogikanen9.demospringreactive.model.CoinBaseResponse;
import com.github.mogikanen9.demospringreactive.model.Purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CoinbaseServiceImpl implements CoinbaseService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ReactiveMongoOperations reactiveMongoTemplate;

    @Override
    public Mono<CoinBaseResponse> getCryptoPrice(String priceName) {
        return webClient.get()
                .uri("https://api.coinbase.com/v2/prices/{crypto}/buy", priceName)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(CoinBaseResponse.class));
    }

    @Override
    public Mono<Purchase> createPurchase(String priceName) {
        //Get crypto price from the coinbase API -> then save a mongoDB document containing the price
        return this.getCryptoPrice(priceName).flatMap(price -> reactiveMongoTemplate.save(
                new Purchase(priceName, price.getData().getAmount(), LocalDateTime.now())
        ));
    }

    @Override
    public Mono<Purchase> getPurchaseById(String id) {
        return reactiveMongoTemplate.findById(id, Purchase.class);
    }

    @Override
    public Flux<Purchase> listAllPurchases() {
        return reactiveMongoTemplate.findAll(Purchase.class);
    }


}
