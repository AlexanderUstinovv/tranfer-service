package com.revolut.transferservice.controller.request;

import java.math.BigDecimal;

public class TransferRequest {

    public TransferRequest() {
    }

    public TransferRequest(long idFrom, long idTo, BigDecimal amount) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.amount = amount;
    }

    public long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(long idFrom) {
        this.idFrom = idFrom;
    }

    public long getIdTo() {
        return idTo;
    }

    public void setIdTo(long idTo) {
        this.idTo = idTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private long idFrom;
    private long idTo;
    private BigDecimal amount;
}