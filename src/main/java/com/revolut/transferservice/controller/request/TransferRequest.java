package com.revolut.transferservice.controller.request;

public class TransferRequest {

    public TransferRequest() {
    }

    public TransferRequest(long idFrom, long idTo, double amount) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private long idFrom;
    private long idTo;
    private double amount;
}