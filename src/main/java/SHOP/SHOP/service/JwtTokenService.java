package SHOP.SHOP.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;



//    public String generateToken(Authentication authentication) {
//        Instant now = Instant.now();
//        String scope = "ROLE_ADMIN";
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(now)
//                .expiresAt(now.plus(1, ChronoUnit.HOURS))
//                .subject(authentication.getName())
//                .claim("scope", scope)
//                .build();
//        var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
//        return this.encoder.encode(encoderParameters).getTokenValue();
//    }
//   public String generateToken(String username, String role) {
//    Instant now = Instant.now();
//    String roles = "ROLE_" + role.toUpperCase(); // e.g., "ROLE_ADMIN"
//
//    JwtClaimsSet claims = JwtClaimsSet.builder()
//            .issuer("self")
//            .issuedAt(now)
//            .expiresAt(now.plus(1, ChronoUnit.HOURS))
//            .subject(username)
//            .claim("roles", roles) // store the role in token
//            .build();
//
//    var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
//    return this.encoder.encode(encoderParameters).getTokenValue();
//}
    //Allowing access by role
public String generateToken(String username, String role) {
    Instant now = Instant.now();
    String authority = "ROLE_" + role.toUpperCase();

    JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(username)
            .claim("roles", List.of(authority))
            .build();

    var encoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
    return this.encoder.encode(encoderParameters).getTokenValue();
}
    public Long extractExpirationTime(String token) {
        Jwt jwt = decoder.decode(token);
        var exp = (Instant) jwt.getClaim("exp");
        return exp.toEpochMilli();
    }
}
