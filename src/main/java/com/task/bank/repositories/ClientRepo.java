package com.task.bank.repositories;

import com.task.bank.entities.Client;
import com.task.bank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    boolean existsClientByUsername(String username);

    Client findClientByUsername(String username);

    Client findClientById(Long id);


}
