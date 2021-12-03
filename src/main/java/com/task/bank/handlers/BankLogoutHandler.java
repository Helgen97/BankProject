package com.task.bank.handlers;

import com.task.bank.services.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class BankLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final ClientService clientService;

    public BankLogoutHandler(ClientService clientService) {
        this.clientService = clientService;
    }



    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication != null){
            super.setDefaultTargetUrl("/?bye");
            User user = (User) authentication.getPrincipal();
            clientService.setUserActivity(user.getUsername(), false);
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
