package com.task.bank.handlers;

import com.task.bank.services.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class BankLoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ClientService clientService;

    public BankLoginHandler(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.setDefaultTargetUrl("/room");
        User user = (User) authentication.getPrincipal();
        clientService.setUserActivity(user.getUsername(), true);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
