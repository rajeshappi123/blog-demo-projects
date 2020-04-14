package com.poc.mapqueuelock.service;

import java.util.Optional;

import com.poc.mapqueuelock.model.Request;

public interface QueueProcessService {

    Optional<Request> getNextAvailable(final String userId) throws QueueProcessServiceException;

    void markAsFulfilled(final String refNumber) throws QueueProcessServiceException;
}