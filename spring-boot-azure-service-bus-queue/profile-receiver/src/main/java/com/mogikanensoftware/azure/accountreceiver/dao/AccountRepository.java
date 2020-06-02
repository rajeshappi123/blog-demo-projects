package com.mogikanensoftware.azure.accountreceiver.dao;

import com.mogikanensoftware.azure.accountreceiver.entity.Account;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String>{
    
}