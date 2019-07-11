package com.github.mogikanen9.demo.rest.api.hello;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.control.Try;

@RestController
@RequestMapping("/api/greeting")
public class HelloControllerResilience4J {

    private RateLimiter greetingApiRateLimiter;

    public HelloControllerResilience4J() {
        this.initRateLimiter();
    }

    public void initRateLimiter() {

        // limit is 2 requests (limitForPeriod) per period (limitRefreshPeriod = 1 sec)
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(2)
                .timeoutDuration(Duration.ofSeconds(1)).build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        // Use registry
        greetingApiRateLimiter = rateLimiterRegistry.rateLimiter("greetingApiRateLimiter");

    }

    @GetMapping("/hello/{name}")
    public ResponseEntity<String> hello(final @PathVariable String name) {

        Supplier<String> helloSupplier = () -> {
            // emulate some work
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return String.format("Hello, %s!", name);
        };

        // Decorate your call to BackendService.doSomething()
        Supplier<String> restrictedSupplier = RateLimiter.decorateSupplier(this.greetingApiRateLimiter, helloSupplier);

        Try<String> callTry = Try.ofSupplier(restrictedSupplier);

        //check success or failure
        if(callTry.isFailure()){           
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }

        return new ResponseEntity<>(callTry.get(), HttpStatus.OK);
         
    }

   
}