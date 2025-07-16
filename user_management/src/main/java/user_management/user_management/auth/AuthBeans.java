package user_management.user_management.auth;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableGlobalAuthentication
public class AuthBeans {

    @Value("${jwt.private-key}") // classpath:keys/private.pem
    private Resource privateKeyRes;

    @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}") // classpath:keys/public.pem
    private Resource publicKeyRes;

    /**
     * Signs JWTs with the RSA private key and publishes the public key in JWK form.
     */
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        String privPem = new String(privateKeyRes.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String pubPem = new String(publicKeyRes.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        RSAPrivateKey privateKey = PemUtils.readPrivateKey(privPem);
        RSAPublicKey publicKey = PemUtils.readPublicKey(pubPem);

        RSAKey jwk = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    /** BCrypt for hashing user passwords. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager that checks credentials against your
     * UserDetailsService.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService uds,
            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }
}
