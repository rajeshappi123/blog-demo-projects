
package com.mogikanensoftware.azure.accountsender.service;

import com.mogikanensoftware.azure.accountsender.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountProducer {

    @Value("${accountQueue.name:accountqueue}")
    private String accountQueueName;

    private JmsTemplate jmsTemplate;

    private ObjectMapper mapper;

    public AccountProducer(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.mapper = new ObjectMapper();
    }

    public void produce(Account account) throws Exception {

        String msg = this.mapper.writeValueAsString(account);
        jmsTemplate.convertAndSend(this.accountQueueName, msg);
        log.info("message sent {}", msg);
    }

}