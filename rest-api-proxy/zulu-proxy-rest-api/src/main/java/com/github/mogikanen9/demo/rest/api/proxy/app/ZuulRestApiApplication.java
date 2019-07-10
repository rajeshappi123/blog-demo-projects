package com.github.mogikanen9.demo.rest.api.proxy.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableHystrixDashboard
@EnableCircuitBreaker
public class ZuulRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulRestApiApplication.class, args);
	}

}