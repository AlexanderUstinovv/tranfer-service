package com.revolut.transferservice;

enum Routes {
    TRANSFER("/transfer");

    Routes(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    private String url;
}
