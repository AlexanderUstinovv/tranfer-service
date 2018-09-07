package com.revolut.transferservice.exception;

public class BadRequest extends RuntimeException {
    public BadRequest(String s) {
        super(s);
    }
}
