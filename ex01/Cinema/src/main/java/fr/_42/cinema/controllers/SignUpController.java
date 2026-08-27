package fr._42.cinema.controllers;

import fr._42.cinema.dto.SignUpRequestDTO;
import fr._42.cinema.models.Role;
import fr._42.cinema.security.CinemaUserDetails;
import fr._42.cinema.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String signUpPage(Authentication authentication, CsrfToken csrfToken, Model model) {
        String redirect = redirectIfAuthenticated(authentication);
        if (redirect != null) {
            return redirect;
        }
        model.addAttribute("_csrf", csrfToken);
        if (!model.containsAttribute("signUpRequest")) {
            model.addAttribute("signUpRequest", new SignUpRequestDTO());
        }
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@Valid @ModelAttribute("signUpRequest") SignUpRequestDTO signUpRequest,
                         BindingResult bindingResult, CsrfToken csrfToken, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("_csrf", csrfToken);
            return "signUp";
        }
        userService.signUp(signUpRequest);
        return "redirect:/signIn";
    }

    private String redirectIfAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CinemaUserDetails userDetails)) {
            return null;
        }
        return userDetails.getUser().getRole() == Role.ADMIN
                ? "redirect:/admin/panel/halls"
                : "redirect:/profile";
    }
}