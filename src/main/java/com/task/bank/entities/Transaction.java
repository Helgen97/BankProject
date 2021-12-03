package com.task.bank.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Sender_ID")
    private Client sender;

    @Column(name = "Send_Currency")
    @Enumerated
    private Currency currencyToSend;

    @Column(name = "Receive_Currency")
    @Enumerated
    private Currency currencyReceive;

    @ManyToOne
    @JoinColumn(name = "Receiver_ID")
    private Client receiver;

    @Column(name = "Value")
    private double value;

    @Column(name = "Comment")
    private String comment;

    @Column(name = "Date")
    private Date date;

    public Transaction() {

    }

    public Transaction(Currency currencyToSend, Currency currencyReceive, double value, String comment) {
        this.currencyToSend = currencyToSend;
        this.currencyReceive = currencyReceive;
        this.value = value;
        this.comment = comment;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Currency getCurrencyToSend() {
        return currencyToSend;
    }

    public void setCurrencyToSend(Currency currencyToSend) {
        this.currencyToSend = currencyToSend;
    }

    public Currency getCurrencyReceive() {
        return currencyReceive;
    }

    public void setCurrencyReceive(Currency currencyReceive) {
        this.currencyReceive = currencyReceive;
    }

    public Client getReceiver() {
        return receiver;
    }

    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
