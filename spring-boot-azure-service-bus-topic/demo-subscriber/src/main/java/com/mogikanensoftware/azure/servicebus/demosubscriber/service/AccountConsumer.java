
package com.mogikanensoftware.azure.servicebus.demosubscriber.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogikanensoftware.azure.servicebus.demosubscriber.model.AccountMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountConsumer {

    private static final String TOPIC_NAME = "accountupdates";

    private ObjectMapper objectMapper;

    private JmsTemplate jmsTemplate;

    @Autowired
    public AccountConsumer(JmsTemplate jmsTemplate) {
        this.objectMapper = new ObjectMapper();
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory", subscription = "demo-subscriber-app")
    public void receiveMessage(Message msg, Session session) {

        log.info("Received message: {}", msg);

        log.info("jmsTemplate.isSessionTransacted -> {} ", jmsTemplate.isSessionTransacted());

        try {

            log.info("session.getAcknowledgeMode(): {}", session.getAcknowledgeMode());

            String body = msg.getBody(String.class);

            AccountMessage am = this.objectMapper.readValue(body, AccountMessage.class);

            log.info("am received -> {}", am);

            // no need - default is autoacknowledge
            // msg.acknowledge();
        } catch (JsonProcessingException | JMSException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }

}