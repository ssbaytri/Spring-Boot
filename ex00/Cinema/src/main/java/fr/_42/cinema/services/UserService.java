package fr._42.cinema.services;

import fr._42.cinema.models.AuthenticationLog;
import fr._42.cinema.models.User;
import fr._42.cinema.repositories.AuthenticationLogRepository;
import fr._42.cinema.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationLogRepository authenticationLogRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthenticationLogRepository authenticationLogRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationLogRepository = authenticationLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String firstName, String lastName, String phoneNumber, String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(firstName, lastName, phoneNumber, email, hashedPassword);
        return userRepository.save(user);
    }

    public List<AuthenticationLog> getAuthenticationLogs(User user) {
        return authenticationLogRepository.findAllByUserOrderByAuthenticatedAtDesc(user);
    }
}
