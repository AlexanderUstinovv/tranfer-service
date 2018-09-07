package com.revolut.transferservice.exception;

public class InsufficientFunds extends RuntimeException {
    public InsufficientFunds(String s) {
        super(s);
    }
}
