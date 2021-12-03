package com.task.bank.services;

import com.task.bank.entities.*;
import com.task.bank.repositories.ClientRepo;
import com.task.bank.repositories.ExchangeRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepo clientRepo;
    private final ExchangeRateRepo rateRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo, ExchangeRateRepo rateRepo) {
        this.clientRepo = clientRepo;
        this.rateRepo = rateRepo;
    }

    @Transactional
    public boolean addNewClient(String username, String password, Roles roles) {
        if (clientRepo.existsClientByUsername(username)) {
            return false;
        } else {
            Client client = new Client(username, password, roles);
            Wallet wallet = new Wallet("UAH");
            client.addWallet(wallet);
            wallet.setClient(client);
            clientRepo.save(client);
            return true;
        }
    }

    @Transactional
    public boolean addWallet(String username, String currency){
        Client client = clientRepo.findClientByUsername(username);
        if (client.ifWalletExist(currency)) return false;

        Wallet wallet = new Wallet(currency);
        wallet.setClient(client);
        client.addWallet(wallet);
        clientRepo.save(client);
        return true;
    }

    @Transactional
    public boolean addMoney(String username, String currency, double value){
        Client client = clientRepo.findClientByUsername(username);

        if(!client.ifWalletExist(currency)) return false;

        client.findWalletByCurrency(currency).increaseMoney(value);
        Transaction transaction = new Transaction(Currency.valueOf(currency), Currency.valueOf(currency), value, "Add money to wallet");
        client.addTransaction(transaction);
        transaction.setSender(client);
        transaction.setReceiver(client);

        clientRepo.save(client);
        return true;
    }

    @Transactional
    public boolean convert(String username, String currencyFrom, String currencyTo, double value){
        Client client = clientRepo.findClientByUsername(username);

        if(!client.ifWalletExist(currencyFrom) || !client.ifWalletExist(currencyTo)) return false;
        if(client.findWalletByCurrency(currencyFrom).getValue() <= value) return false;

        client.convertMoney(currencyFrom, currencyTo, value, rateRepo.findExchangeRateByCcy(currencyFrom), rateRepo.findExchangeRateByCcy(currencyTo));
        clientRepo.save(client);
        return true;
    }

    @Transactional
    public String sendMoney(String username, String currencyFrom, double value, String receiver, String currencyTo, String comment){
        Client sender = clientRepo.findClientByUsername(username);
        Client receiverClient = clientRepo.findClientByUsername(receiver);

        String isError = sendError(sender, currencyFrom, receiverClient, currencyTo, value);
        if(isError.equals("ok")) {

            Transaction transaction = new Transaction(Currency.valueOf(currencyFrom), Currency.valueOf(currencyTo), value, comment);
            sender.addTransaction(transaction);
            transaction.setSender(sender);
            transaction.setReceiver(receiverClient);
            sender.findWalletByCurrency(currencyFrom).decreaseMoney(value);
            receiverClient.convertMoneyWhenReceive(currencyFrom, currencyTo, value, rateRepo.findExchangeRateByCcy(currencyFrom), rateRepo.findExchangeRateByCcy(currencyTo));

            clientRepo.save(sender);
            clientRepo.save(receiverClient);
        }
        return isError;
    }

    private String sendError(Client sender, String currencyFrom, Client receiverClient, String currencyTo, double value){
        if (sender.findWalletByCurrency(currencyFrom) == null) {
            return "You dont have such currency wallet";
        }
        if (sender.findWalletByCurrency(currencyFrom).getValue() < value) {
            return "You dont have enough money";
        }
        if (receiverClient == null) {
            return "Receiver not found";
        }
        if (receiverClient.findWalletByCurrency(currencyTo) == null) {
            return "Receiver doesn't have this currency wallet";
        }
        return "ok";
    }

    @Transactional
    public void changeUserRole(long id, String newRole){
        Client client = getClientByID(id);
        client.setRole(Roles.valueOf(newRole));
        clientRepo.save(client);
    }

    @Transactional
    public void setUserActivity(String username, boolean isActive){
        Client client = findClientByUserName(username);
        client.setActive(isActive);
        clientRepo.save(client);
    }

    @Transactional
    public Client findClientByUserName(String username) {
        return clientRepo.findClientByUsername(username);
    }

    @Transactional
    public List<Client> findAllClients(){
        return clientRepo.findAll();
    }

    @Transactional
    public Client getClientByID(Long id){
        return clientRepo.findClientById(id);
    }
}
