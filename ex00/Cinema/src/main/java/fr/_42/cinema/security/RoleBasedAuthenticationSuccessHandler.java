package fr._42.cinema.security;

import fr._42.cinema.models.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CinemaUserDetails userDetails = (CinemaUserDetails) authentication.getPrincipal();
        String targetUrl = userDetails.getUser().getRole() == Role.ADMIN ? "/admin/panel/halls" : "/profile";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
