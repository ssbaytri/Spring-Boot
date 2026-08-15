package fr._42.cinema.services;

import fr._42.cinema.models.User;
import fr._42.cinema.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(String firstName, String lastName, String phoneNumber, String email, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(firstName, lastName, phoneNumber, email, hashedPassword);
        return userRepository.save(user);
    }
}
