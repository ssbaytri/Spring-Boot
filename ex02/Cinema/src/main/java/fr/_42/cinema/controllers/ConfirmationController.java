package fr._42.cinema.controllers;

import fr._42.cinema.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class ConfirmationController {

    private final UserService userService;

    public ConfirmationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/confirm/{token}")
    public String confirm(@PathVariable UUID token) {
        boolean confirmed = userService.confirm(token);
        return confirmed ? "redirect:/signIn?confirmed" : "redirect:/signIn?invalidToken";
    }

}
