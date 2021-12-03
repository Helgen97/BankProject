package com.task.bank.controlers;

import com.task.bank.entities.Client;
import com.task.bank.repositories.TransactionsRepo;
import com.task.bank.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsersController {

    private final ClientService clientService;
    private final TransactionsRepo transactionsRepo;

    @Autowired
    public UsersController(ClientService clientService, TransactionsRepo transactionsRepo) {
        this.clientService = clientService;
        this.transactionsRepo = transactionsRepo;
    }

    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("users", clientService.findAllClients());
        return "options/admin";
    }

    @GetMapping("/open")
    public String openWallet() {
        return "options/wallet";
    }

    @PostMapping("/open")
    public String openWallet(@RequestParam String currency, Model model) {
        if (!clientService.addWallet(getCurrentUser().getUsername(), currency)) {
            model.addAttribute("error", "This type of wallet already exist!");
            return "options/wallet";
        }
        return "redirect:/room";
    }

    @GetMapping("/add")
    public String addMoney() {
        return "options/addMoney";
    }

    @PostMapping("/add")
    public String addMoney(@RequestParam String currency, @RequestParam double value, Model model) {
        if (!clientService.addMoney(getCurrentUser().getUsername(), currency, value)) {
            model.addAttribute("error", "You dont such type of wallet!");
            return "options/addMoney";
        }
        return "redirect:/room";
    }

    @GetMapping("/convert")
    public String convert() {
        return "options/converting";
    }

    @PostMapping("/convert")
    public String convert(@RequestParam String currencyFrom,
                          @RequestParam String currencyTo,
                          @RequestParam double value,
                          Model model) {
        if (!clientService.convert(getCurrentUser().getUsername(), currencyFrom, currencyTo, value)) {
            model.addAttribute("error", "You dont have such type of wallet or dont have enough money!");
            return "options/converting";
        }
        return "redirect:/room";
    }

    @GetMapping("/send")
    public String send() {
        return "options/sendMoney";
    }

    @PostMapping("/send")
    public String send(@RequestParam String currencyFrom,
                       @RequestParam Double value,
                       @RequestParam String receiver,
                       @RequestParam String currencyTo,
                       @RequestParam String comment,
                       Model model) {
        String isError = clientService.sendMoney(getCurrentUser().getUsername(), currencyFrom, value, receiver, currencyTo, comment);

        if (!isError.equals("ok")) {
            model.addAttribute("error", isError);
            return "options/sendMoney";
        }
        return "redirect:/room";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("transactions", transactionsRepo.findAllByReceiver_Username(getCurrentUser().getUsername()));

        return "options/transactionLook";
    }

    @GetMapping("/change_role")
    public String changeRole(@RequestParam long id, Model model){
        Client client = clientService.getClientByID(id);
        model.addAttribute("username", client.getUsername());
        model.addAttribute("role", client.getRole());
        model.addAttribute("id", client.getId());
        return "options/changeRole";
    }

    @PostMapping("/change_role")
    public String changeRole(@RequestParam String newRole, @RequestParam long id, Model model){
        clientService.changeUserRole(id, newRole);
        return "redirect:/admin";
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
