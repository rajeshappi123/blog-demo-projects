
package com.mogikanensoftware.azure.accountreceiver.service;

import com.mogikanensoftware.azure.accountreceiver.dao.AccountRepository;
import com.mogikanensoftware.azure.accountreceiver.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(transactionManager = "jpaTransactionManager")
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account log(Account account) {
        return this.accountRepository.save(account);
    }

}