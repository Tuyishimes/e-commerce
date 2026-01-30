package SHOP.SHOP.controller;
import lombok.extern.slf4j.Slf4j;
import SHOP.SHOP.dto.AuthRequest;
import SHOP.SHOP.dto.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import SHOP.SHOP.dto.RegisterRequest;
import SHOP.SHOP.dto.UserDto;
import SHOP.SHOP.model.User;
import SHOP.SHOP.repository.UserRepository;
import SHOP.SHOP.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shop/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final  UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        log.info("Register customer have started : {}", request);

        String token = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        return ResponseEntity.ok(new AuthResponse(token));

    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request.getEmail(), request.getPassword());
        log.info("Requesting token "+request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
       // log.info("Tokene granted "+request.getEmail());
    }
    @GetMapping("/profile")

    public ResponseEntity<?> profile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        log.debug("Knowing profiles "+user.getName());

        return ResponseEntity.ok(Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
        ));

    }


}
