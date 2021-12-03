package com.task.bank.controlers;

import com.task.bank.entities.Client;
import com.task.bank.entities.Roles;
import com.task.bank.repositories.ExchangeRateRepo;
import com.task.bank.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final ClientService clientService;
    private final ExchangeRateRepo rateRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(ClientService clientService, ExchangeRateRepo rateRepo, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.rateRepo = rateRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String main(@RequestParam(required = false) String bye, Model model) {
        if (bye != null) {
            model.addAttribute("bye", "Good Bye!");
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        Model model) {
        if (error != null) model.addAttribute("error", "Bad username or password");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/room")
    public String userRoom(Model model) {
        Client client = clientService.findClientByUserName(getCurrentUser().getUsername());

        model.addAttribute("userRole", client.getRole().toString().equals("ROLE_ADMIN") || client.getRole().toString().equals("ROLE_MODERATOR") );
        model.addAttribute("username", client.getUsername());
        model.addAttribute("wallets", client.getWallets());
        model.addAttribute("sum", client.sumInUAH(rateRepo.findAll()));

        return "userRoom";
    }

    @GetMapping("/denied")
    public String deniedPage(Model model) {
        Client client = clientService.findClientByUserName(getCurrentUser().getUsername());
        model.addAttribute("role", client.getRole());
        return "denied";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (!clientService.addNewClient(username, passwordEncoder.encode(password), Roles.USER)) {
            model.addAttribute("error", "User with this nickname already exist!");
            return "register";
        } else {
            return "redirect:/login";
        }
    }


    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
