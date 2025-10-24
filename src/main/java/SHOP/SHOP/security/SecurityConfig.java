package SHOP.SHOP.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object rolesClaim = jwt.getClaim("roles");
            if (rolesClaim instanceof List) {
                List<?> roles = (List<?>) rolesClaim;
                return roles.stream()
                        .map(Object::toString)
                        .map(role -> (GrantedAuthority) () -> role) // create authority
                        .collect(Collectors.toList());
            }
            return List.of();
        });
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/shop/auth/token", "/shop/auth/register", "/api/public/**").permitAll()
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/api/products/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/shop/admin/**").hasRole("ADMIN")
                        .requestMatchers("/shop/api/products/public/**", "/shop/api/categories").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shop/user/**").hasRole("USER")
                        .requestMatchers("/shop/auth/profile").authenticated()
                        .requestMatchers("/api/shop/orders").hasRole("USER")
                        .requestMatchers("/api/shop/orders/orderBy/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/shop/orders/update/**").hasRole("ADMIN")
                        .requestMatchers("/shop/api/payment/pay/**").hasRole("USER")
                        .requestMatchers("/shop/api/payment/admin/all").hasRole("ADMIN")
                        .requestMatchers("/shop/api/payment/status/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthConverter)
                ));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
}
