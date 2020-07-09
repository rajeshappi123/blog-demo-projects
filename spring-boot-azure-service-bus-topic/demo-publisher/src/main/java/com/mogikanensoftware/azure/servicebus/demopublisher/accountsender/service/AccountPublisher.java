
package com.mogikanensoftware.azure.servicebus.demopublisher.accountsender.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogikanensoftware.azure.servicebus.demopublisher.accountsender.model.Account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountPublisher {

    @Value("${accountTopic.name:accountupdates}")
    private String accountTopicName;

    private JmsTemplate jmsTemplate;

    private ObjectMapper mapper;

    public AccountPublisher(final JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.mapper = new ObjectMapper();
    }

    public void publish(Account account) throws Exception {

        String msg = this.mapper.writeValueAsString(account);
        jmsTemplate.convertAndSend(this.accountTopicName, msg);
        log.info("message published {}", msg);
    }

}