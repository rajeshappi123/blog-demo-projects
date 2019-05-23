package com.github.mogikanen9.demo.rest.api.hello.wrlrestapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HelloControllerGuavaTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService eService = Executors.newFixedThreadPool(20);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(2000);
        RestTemplate client = new RestTemplate(factory);

        List<Future<String>> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Future<String> task = eService.submit(() -> {
                String body = client.getForObject("http://localhost:8080/api/hello/ping", String.class);
                assertThat(body).isEqualTo("pong");
                return body;
            });
            tasks.add(task);
        }

        System.out.println("all tasks created/scheduled");

        for (Future<String> task : tasks) {
            String respBody = task.get();
            System.out.println(String.format("respBody->%s", respBody));
        }

        System.out.println("all requests submitted");

        eService.shutdown();

    }
}