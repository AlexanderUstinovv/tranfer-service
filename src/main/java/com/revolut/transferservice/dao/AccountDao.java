package com.revolut.transferservice.dao;

import com.revolut.transferservice.model.Account;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(long id);
    void update(Account account);
}
