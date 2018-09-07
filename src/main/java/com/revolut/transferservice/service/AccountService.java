package com.revolut.transferservice.service;

public interface AccountService {
    void transfer(long idFrom, long idTo, double amount);
}
