package fr._42.cinema.controllers;

import fr._42.cinema.models.Role;
import fr._42.cinema.security.CinemaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    @GetMapping("/signIn")
    public String signInPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CinemaUserDetails userDetails) {
            return userDetails.getUser().getRole() == Role.ADMIN
                    ? "redirect:/admin/panel/halls"
                    : "redirect:/profile";
        }
        return "signIn";
    }
}
