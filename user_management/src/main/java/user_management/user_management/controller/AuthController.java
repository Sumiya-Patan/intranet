package user_management.user_management.controller;

import lombok.RequiredArgsConstructor;
import user_management.user_management.auth.AppUserPrincipal;
import user_management.user_management.service.AuthorizationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder          jwtEncoder;
    private final AuthorizationService authorizationService;   // your cached permission service

    // ---------- DTOs ----------
    public record LoginRequest(String email, String password) {}
    public record TokenResponse(String access_token,
                                String token_type,
                                long   expires_in) {}

    // ---------- Endpoint ----------
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        System.out.println("Login attempt for user: " + request.email());

        /* 1) Authenticate credentials against UserDetailsService */
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),
                                                         request.password()));

        System.out.println("Authenticated user: " + auth.getName());

        /* 2) Cast principal to our custom AppUserPrincipal */
        AppUserPrincipal principal = (AppUserPrincipal) auth.getPrincipal();
        Long   userId = principal.getUserId();
        String email  = principal.getEmail();

        /* 3) Build JWT claims */
        Instant  now   = Instant.now();
        Duration ttl   = Duration.ofMinutes(30);
        Instant  exp   = now.plus(ttl);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("user-management-api")
                .issuedAt(now)
                .expiresAt(exp)
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", principal.getAuthorities().stream()
                                         .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                                         .toList())
                .claim("perms", authorizationService.permissionsFor(userId))
                .build();

        /* 4) Encode & return */
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims))
                                 .getTokenValue();

        return ResponseEntity.ok(
                new TokenResponse(token, "Bearer", ttl.toSeconds()));
    }
}
