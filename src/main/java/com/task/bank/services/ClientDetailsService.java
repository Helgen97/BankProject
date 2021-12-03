package com.task.bank.services;

import com.task.bank.entities.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientDetailsService implements UserDetailsService {
    private final ClientService clientService;

    public ClientDetailsService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientService.findClientByUserName(username);
        if (client == null) {
            throw new UsernameNotFoundException(username + "not found!");
        }
        List<GrantedAuthority> grantedAuthorityList = Arrays.asList(new SimpleGrantedAuthority(client.getRole().toString()));
        return new User(client.getUsername(), client.getPassword(), grantedAuthorityList);
    }
}
