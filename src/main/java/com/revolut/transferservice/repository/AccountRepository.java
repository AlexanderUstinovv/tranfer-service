package com.revolut.transferservice.repository;

import com.revolut.transferservice.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(long id);
    void update(Account account);
}
