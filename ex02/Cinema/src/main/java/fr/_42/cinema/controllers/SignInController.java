package fr._42.cinema.controllers;

import fr._42.cinema.models.Role;
import fr._42.cinema.security.CinemaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignInController {

    @GetMapping("/signIn")
    public String signInPage(Authentication authentication, CsrfToken csrfToken,
                             @RequestParam(required = false) String error,
                             @RequestParam(required = false) String disabled,
                             @RequestParam(required = false) String registered,
                             @RequestParam(required = false) String confirmed,
                             @RequestParam(required = false) String invalidToken,
                             Model model) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CinemaUserDetails userDetails) {
            return userDetails.getUser().getRole() == Role.ADMIN
                    ? "redirect:/admin/panel/halls"
                    : "redirect:/profile";
        }
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("loginError", error != null);
        model.addAttribute("disabledError", disabled != null);
        model.addAttribute("registered", registered != null);
        model.addAttribute("confirmed", confirmed != null);
        model.addAttribute("invalidToken", invalidToken != null);
        return "signIn";
    }
}