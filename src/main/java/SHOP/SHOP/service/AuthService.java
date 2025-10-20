package SHOP.SHOP.service;

import SHOP.SHOP.model.User;
import SHOP.SHOP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user and return a JWT token.
     */
    public String register(String name, String email, String rawPassword, String role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role.toUpperCase()); // ✅ role stored as plain string

        userRepository.save(user);

        // 🔐 Generate JWT using the provided role
        return jwtTokenService.generateToken(user.getEmail(), role);
    }

    /**
     * Authenticate existing user and return a new JWT token.
     */
    public String authenticate(String email, String rawPassword) {
        // This will throw AuthenticationException if credentials are invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );

        // Get user from DB to retrieve role for the token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 🔐 Pass the stored role directly to JWT generator
        return jwtTokenService.generateToken(user.getEmail(), user.getRole());
    }
}
