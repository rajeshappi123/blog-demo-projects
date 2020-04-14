package com.poc.mapqueuelock;

import java.util.Date;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.poc.mapqueuelock.model.Request;
import com.poc.mapqueuelock.repo.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MapQueueLockApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MapQueueLockApplication.class, args);
	}

	@Autowired
	private RequestRepository repository;

	@Override
	public void run(String... args) throws Exception {

		FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-CA"), new RandomService());

		Faker faker = new Faker();
		for (int i = 0; i < 50; i++) {
			Request req = new Request();

			req.setFirstName(faker.name().firstName());

			req.setLastName(faker.name().lastName());

			req.setRefNumber(fakeValuesService.bothify("?#??###??#?"));
			req.setRequestDate(new Date());

			req.setEmail(
					req.getFirstName() + "." + req.getLastName() + fakeValuesService.bothify("##??@mailinator.com"));

			repository.save(req);
		}

	}

}
