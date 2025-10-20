package SHOP.SHOP.security;

import SHOP.SHOP.repository.JwtKeyRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtKeyRepository jwtKeyRepository;


    private  String getsecretfromDb(){
        String secretKey=jwtKeyRepository.findLatestKey();
        if(secretKey ==null || secretKey.isEmpty()){
            throw new IllegalArgumentException("No secrete key found in dB");
        }
        return secretKey;
    }
    @Bean
    public JwtEncoder jwtEncoder(){
        String secretKey=getsecretfromDb();
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        String secretKey=getsecretfromDb();
        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),"HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

}
