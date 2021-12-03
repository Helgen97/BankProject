package com.task.bank.configs;

import com.task.bank.handlers.BankLoginHandler;
import com.task.bank.handlers.BankLogoutHandler;
import com.task.bank.services.ClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final BankLogoutHandler bankLogoutHandler;
    private final BankLoginHandler bankLoginHandler;

    public SecurityConfig(ClientDetailsService clientDetailsService, PasswordEncoder passwordEncoder, BankLogoutHandler bankLogoutHandler, BankLoginHandler bankLoginHandler) {
        this.clientDetailsService = clientDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.bankLogoutHandler = bankLogoutHandler;
        this.bankLoginHandler = bankLoginHandler;
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(clientDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/admin")
                        .hasAnyRole("ADMIN", "MODERATOR")
                    .antMatchers("/open", "/add", "/convert", "/send", "/list", "/room", "/logout")
                        .hasAnyRole("ADMIN", "USER", "MODERATOR")
                    .antMatchers("/")
                        .permitAll()
                    .antMatchers("/change_role")
                        .hasAnyRole("ADMIN")
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/denied")
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("sec_username")
                    .passwordParameter("sec_password")
                    .failureUrl("/login?error")
                    .permitAll()
                    .successHandler(bankLoginHandler)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .permitAll()
                    .logoutSuccessHandler(bankLogoutHandler);
    }
}
