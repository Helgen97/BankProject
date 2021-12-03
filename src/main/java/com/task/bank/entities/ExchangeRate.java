package com.task.bank.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Entity
@Table(name = "ExchangeRates")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Currency", nullable = false)
    private String ccy;

    @Column(name = "Base_Currency")
    private String base_ccy;

    @Column(name = "Buy")
    private double buy;

    @Column(name = "Sale")
    private double sale;

    public ExchangeRate() {

    }

    public Long getId() {
        return id;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getBase_ccy() {
        return base_ccy;
    }

    public void setBase_ccy(String base_ccy) {
        this.base_ccy = base_ccy;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public static ExchangeRate[] getExchangesRate() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = null;
        try {
            URL obj = new URL("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (InputStream os = conn.getInputStream()) {
                json = new String(os.readAllBytes(), StandardCharsets.UTF_8);
            }
        }catch (IOException e){
            e.getCause();
        }
        return gson.fromJson(json, ExchangeRate[].class);
    }
}