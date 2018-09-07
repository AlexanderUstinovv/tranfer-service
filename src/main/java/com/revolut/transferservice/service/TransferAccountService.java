package com.revolut.transferservice.service;

import com.revolut.transferservice.exception.InsufficientFunds;
import com.revolut.transferservice.exception.NotFoundException;
import com.revolut.transferservice.model.Account;
import com.revolut.transferservice.repository.AccountRepository;

import java.util.Optional;

public class TransferAccountService implements AccountService {

    public TransferAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void transfer(long idFrom, long idTo, double amount) throws NotFoundException {
        Account accountFrom = getAccountById(idFrom);
        Account accountTo = getAccountById(idTo);

        if (accountFrom.getBalance() - amount >= 0) {
            accountFrom.setBalance(accountFrom.getBalance() - amount);
            accountTo.setBalance(accountTo.getBalance() + amount);
            accountRepository.update(accountFrom);
            accountRepository.update(accountTo);
        } else {
            throw new InsufficientFunds("Insufficient funds on account " + accountFrom.getId());
        }
    }

    private Account getAccountById(long id) throws NotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.orElseThrow(() -> new NotFoundException("Account " + id + " doesn't exist"));
    }

    private AccountRepository accountRepository;
}
