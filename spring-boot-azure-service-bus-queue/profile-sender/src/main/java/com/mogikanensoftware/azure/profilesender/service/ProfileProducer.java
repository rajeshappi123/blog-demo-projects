
package com.mogikanensoftware.azure.profilesender.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.servicebus.IQueueClient;
import com.microsoft.azure.servicebus.Message;
import com.mogikanensoftware.azure.profilesender.model.Profile;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProfileProducer {

    private final IQueueClient iQueueClient;

    private ObjectMapper mapper;

    public ProfileProducer(final IQueueClient iQueueClient) {
        this.iQueueClient = iQueueClient;
        this.mapper = new ObjectMapper();
    }

    public void produce(Profile profile) throws Exception {
        Message msg = new Message(mapper.writeValueAsString(profile));
        this.iQueueClient.send(msg);
        log.info("message sent {}", msg);
    }

}