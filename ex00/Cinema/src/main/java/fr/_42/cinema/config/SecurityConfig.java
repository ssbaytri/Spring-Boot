package fr._42.cinema.config;

import fr._42.cinema.repositories.AuthenticationLogRepository;
import fr._42.cinema.security.CinemaUserDetailsService;
import fr._42.cinema.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(AuthenticationLogRepository authenticationLogRepository) {
        return new RoleBasedAuthenticationSuccessHandler(authenticationLogRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CinemaUserDetailsService userDetailsService, AuthenticationLogRepository authenticationLogRepository)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/panel/**").hasRole("ADMIN")
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/session/search").authenticated()
                        .requestMatchers("/films/*/chat", "/films/*/chat/messages").authenticated()
                        .requestMatchers("/signIn", "/signUp").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/signIn")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler(authenticationLogRepository))
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("cinema-remember-me-key")
                        .tokenValiditySeconds(14 * 24 * 60 * 60)
                )
                .logout(logout -> logout
                        .logoutUrl("/signOut")
                        .logoutSuccessUrl("/signIn")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }
}