package com.task.bank.entities;

public enum Currency {
    UAH("UAH"),
    USD("USD"),
    EUR("EUR");

    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }


}
