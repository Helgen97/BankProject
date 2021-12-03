package com.task.bank.repositories;

import com.task.bank.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, Long> {

    ExchangeRate findExchangeRateByCcy(String currency);
}
