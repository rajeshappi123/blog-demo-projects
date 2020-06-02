
package com.mogikanensoftware.azure.accountreceiver.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogikanensoftware.azure.accountreceiver.entity.Account;
import com.mogikanensoftware.azure.accountreceiver.entity.Branch;
import com.mogikanensoftware.azure.accountreceiver.model.AccountMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountConsumer {

    private static final String QUEUE_NAME = "accountqueue";

    AccountService accountService;

    private ObjectMapper objectMapper;

    private JmsTemplate jmsTemplate;

    @Autowired
    public AccountConsumer(AccountService accountService, JmsTemplate jmsTemplate) {
        this.objectMapper = new ObjectMapper();
        this.accountService = accountService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory", concurrency = "2")
    public void receiveMessage(Message msg, Session session) {

        log.info("Received message: {}", msg);

        log.debug("jmsTemplate.isSessionTransacted -> {} ", jmsTemplate.isSessionTransacted());

        try {

            log.debug("session.getAcknowledgeMode(): {}", session.getAcknowledgeMode());

            String body = msg.getBody(String.class);

            AccountMessage am = this.objectMapper.readValue(body, AccountMessage.class);
            if (am.getProfileId().equals("123")) {
                throw new RuntimeException("Invalid profile");
            }

            Account ae = new Account();
            ae.setBankName(am.getBankName());
            ae.setNumber(am.getNumber());
            ae.setOpenDate(am.getOpenDate());

            if (am.getBranch() != null) {
                ae.setBranch(new Branch(am.getBranch().getName(), am.getBranch().getCode(), am.getBranch().getLocation()));
            }

            this.accountService.log(ae);

            msg.acknowledge();
        } catch (JsonProcessingException | JMSException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }

}