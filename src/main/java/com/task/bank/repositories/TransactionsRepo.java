package com.task.bank.repositories;

import com.task.bank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepo extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByReceiver_Username(String username);
}
