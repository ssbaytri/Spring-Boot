package fr._42.cinema.controllers;

import fr._42.cinema.models.Role;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String signUpPage(Authentication authentication) {
        String redirect = redirectIfAuthenticated(authentication);
        return redirect != null ? redirect : "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String password
    ) {
        if (isBlank(firstName) || isBlank(lastName) || isBlank(phoneNumber)
                || isBlank(email) || isBlank(password)) {
            return "redirect:/signUp?error";
        }

        userService.signUp(firstName, lastName, phoneNumber, email, password);
        return "redirect:/signIn";
    }

    private String redirectIfAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof CinemaUserDetails userDetails)) {
            return null;
        }
        return userDetails.getUser().getRole() == Role.ADMIN ? "redirect:/admin/panel/halls" : "redirect:/profile";
    }

    private boolean isBlank(String s ) {
        return s == null || s.trim().isEmpty();
    }
}
