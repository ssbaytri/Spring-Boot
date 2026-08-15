package fr._42.cinema.security;

import fr._42.cinema.models.Role;
import fr._42.cinema.models.AuthenticationLog;
import fr._42.cinema.repositories.AuthenticationLogRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class RoleBasedAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationLogRepository authenticationLogRepository;

    public RoleBasedAuthenticationSuccessHandler(AuthenticationLogRepository authenticationLogRepository) {
        this.authenticationLogRepository = authenticationLogRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CinemaUserDetails userDetails = (CinemaUserDetails) authentication.getPrincipal();
        authenticationLogRepository.save(new AuthenticationLog(userDetails.getUser(), LocalDateTime.now(), request.getRemoteAddr()));
        String targetUrl = userDetails.getUser().getRole() == Role.ADMIN ? "/admin/panel/halls" : "/profile";
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
