package com.task.bank.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true)
    private String username;


    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Active")
    private boolean isActive;

    @Enumerated
    private Roles role;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Wallet> wallets = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<Transaction> clientTransactions = new ArrayList<>();

    public Client() {

    }

    public Client(String name, String password, Roles role) {
        this.username = name;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public List<Transaction> getTransactions() {
        return clientTransactions;
    }

    public List<Transaction> getClientTransactions() {
        return clientTransactions;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addWallet(Wallet wallet) {
        if (!wallets.contains(wallet)) wallets.add(wallet);
    }

    public void addTransaction(Transaction transaction) {
        if (!clientTransactions.contains(transaction)) clientTransactions.add(transaction);
    }

    public Wallet findWalletByCurrency(String currency) {
        for (Wallet wallet : wallets) {
            if (wallet.getCurrency().name().equals(currency)) return wallet;
        }
        return null;
    }

    public boolean ifWalletExist(String currency) {
        for (Wallet wallet : wallets) {
            if (wallet.getCurrency().name().equals(currency)) return true;
        }
        return false;
    }

    public void convertMoney(String currencyF, String currencyT, double value, ExchangeRate f, ExchangeRate t) {
        if (currencyF.equals(Currency.UAH.getCurrency()) & !currencyT.equals(Currency.UAH.getCurrency())) {
            findWalletByCurrency(currencyF).decreaseMoney(value);
            findWalletByCurrency(currencyT).increaseMoney(value * t.getSale());
        } else if (!currencyF.equals(Currency.UAH.getCurrency()) & !currencyT.equals(Currency.UAH.getCurrency())) {
            findWalletByCurrency(currencyF).decreaseMoney(value);
            findWalletByCurrency(currencyT).increaseMoney(value * f.getBuy() / t.getSale());
        } else if (!currencyF.equals(Currency.UAH.getCurrency()) & currencyT.equals(Currency.UAH.getCurrency())) {
            findWalletByCurrency(currencyF).decreaseMoney(value);
            findWalletByCurrency(currencyT).increaseMoney(value * f.getBuy());
        } else if(currencyF.equals(Currency.UAH.getCurrency()) & currencyT.equals(Currency.UAH.getCurrency())){
            findWalletByCurrency(currencyF).decreaseMoney(value);
            findWalletByCurrency(currencyT).increaseMoney(value);
        }
    }

    public void convertMoneyWhenReceive(String currencyF, String currencyT, double value, ExchangeRate f, ExchangeRate t){
        if (currencyF.equals(Currency.UAH.getCurrency()) & currencyT.equals(Currency.UAH.getCurrency())){
            findWalletByCurrency(currencyT).increaseMoney(value);
        }else if (!currencyF.equals(Currency.UAH.getCurrency()) & currencyT.equals(Currency.UAH.getCurrency())){
            findWalletByCurrency(currencyT).increaseMoney(value * f.getBuy());
        }else if (!currencyF.equals(Currency.UAH.getCurrency()) & !currencyT.equals(Currency.UAH.getCurrency())){
            findWalletByCurrency(currencyT).increaseMoney(value * f.getBuy() / t.getSale());
        } else {
            findWalletByCurrency(currencyT).increaseMoney(value * t.getSale());
        }
    }

    public double sumInUAH(List<ExchangeRate> rates){
        double sum = 0;
        sum += findWalletByCurrency("UAH").getValue();

        for (ExchangeRate rate: rates){
            for(Wallet wallet: getWallets()){
                if(wallet.getCurrency().name().equals(rate.getCcy())){
                    sum += wallet.getValue() * rate.getBuy();
                }
            }
        }
        return sum;
    }
}
