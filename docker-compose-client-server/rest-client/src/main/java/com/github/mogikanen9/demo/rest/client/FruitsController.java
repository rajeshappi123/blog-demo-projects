package com.github.mogikanen9.demo.rest.client;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FruitsController {

    @Value("${fruits.rest.api.url}")
    String fruitsRestApiUrl;

    @GetMapping("/v1/fruits/{name}")
    public Fruit get(@PathVariable String name) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Fruit>> response = restTemplate.exchange(fruitsRestApiUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Fruit>>() {
                });
        List<Fruit> fruits = response.getBody().stream().filter(f -> f.getName().toLowerCase().equals(name.toLowerCase())).collect(Collectors.toList());
        System.out.println(String.format("Trying to find fruit->%s",name));
        
        return fruits.isEmpty()?new Fruit("N/A","N/A"):fruits.get(0);
    }
}