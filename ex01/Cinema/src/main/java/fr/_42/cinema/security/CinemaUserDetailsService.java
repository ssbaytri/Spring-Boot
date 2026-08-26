package fr._42.cinema.security;

import fr._42.cinema.CinemaApplication;
import fr._42.cinema.models.User;
import fr._42.cinema.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CinemaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CinemaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("No user found with email: " + email)
        );
        return new CinemaUserDetails(user);
    }
}
