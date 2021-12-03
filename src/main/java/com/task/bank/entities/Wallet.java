package com.task.bank.entities;

import javax.persistence.*;

@Entity
@Table(name = "Wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Currency")
    @Enumerated
    private Currency currency;

    @Column(name = "Value")
    private double value;

    @ManyToOne
    @JoinColumn(name = "Client_ID")
    private Client client;

    public Wallet() {

    }

    public Wallet(String currency) {
        this.currency = Currency.valueOf(currency);
        this.value = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void increaseMoney(double value) {
        this.value += value;
    }

    public void decreaseMoney(double value) {
        this.value -= value;
    }
}
