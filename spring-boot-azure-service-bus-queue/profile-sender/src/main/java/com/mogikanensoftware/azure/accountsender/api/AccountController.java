
package com.mogikanensoftware.azure.accountsender.api;

import com.mogikanensoftware.azure.accountsender.model.Account;
import com.mogikanensoftware.azure.accountsender.service.AccountProducer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountController {

    private AccountProducer acountProducer;

    @PostMapping("/send")
    public ResponseEntity <Account> send(@RequestBody Account account) {
        try {
            this.acountProducer.produce(account);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity <>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }

    public AccountController(AccountProducer acountProducer) {
        this.acountProducer = acountProducer;
    }
}