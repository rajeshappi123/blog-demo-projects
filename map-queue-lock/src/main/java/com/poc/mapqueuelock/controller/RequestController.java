package com.poc.mapqueuelock.controller;

import java.util.Optional;

import com.poc.mapqueuelock.model.Request;
import com.poc.mapqueuelock.repo.RequestRepository;
import com.poc.mapqueuelock.service.QueueProcessService;
import com.poc.mapqueuelock.service.QueueProcessServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RequestController {

    private QueueProcessService queueService;

    private RequestRepository requestRepo;

    public RequestController(@Autowired QueueProcessService requestService, RequestRepository requestRepo) {
        this.queueService = requestService;
        this.requestRepo = requestRepo;
    }

    @PostMapping(path = "/request")
    public ResponseEntity<Request> submitNewRequest(@RequestBody Request newRequest) {
        Request savedRequest = requestRepo.save(newRequest);
        return new ResponseEntity<Request>(savedRequest, HttpStatus.CREATED);
    }

    @GetMapping(path = "/next/{userId}")
    public ResponseEntity<Request> getNextRequestFromWaitingQueue(@PathVariable String userId)
            throws QueueProcessServiceException {
        Optional<Request> nextRequestFromQueue = queueService.getNextAvailable(userId);
        if (nextRequestFromQueue.isPresent()) {
            return new ResponseEntity<Request>(nextRequestFromQueue.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Request>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(path = "/fulfill/{userId}")
    public ResponseEntity<String> fulfillRequest(@RequestBody Request newRequest, @PathVariable String userId)
            throws QueueProcessServiceException {
        this.queueService.markAsFulfilled(newRequest.getRefNumber());
        return new ResponseEntity<>(newRequest.getRefNumber(), HttpStatus.CREATED);
    }

}