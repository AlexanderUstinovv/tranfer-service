package com.revolut.transferservice.service;

import java.math.BigDecimal;

public interface AccountService {
    void transfer(long idFrom, long idTo, BigDecimal amount);
}
