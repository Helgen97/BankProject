package com.task.bank.configs;

import com.task.bank.entities.ExchangeRate;
import com.task.bank.entities.Roles;
import com.task.bank.repositories.ExchangeRateRepo;
import com.task.bank.services.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BankConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner start(final ClientService clientService,
                                   final PasswordEncoder encoder,
                                   final ExchangeRateRepo rateRepo){

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                clientService.addNewClient("admin", encoder.encode("admin"), Roles.ADMIN);
                rateRepo.deleteAll();
                ExchangeRate[] rateList = ExchangeRate.getExchangesRate();
                rateRepo.saveAll(Arrays.asList(rateList).subList(0, rateList.length - 2));
            }
        };
    }
}
