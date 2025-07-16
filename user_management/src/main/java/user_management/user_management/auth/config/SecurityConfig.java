package user_management.user_management.auth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.core.convert.converter.Converter;

import lombok.RequiredArgsConstructor;
import user_management.user_management.service.AuthorizationService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthorizationService authorizationService; // your cache service

    @Bean
    SecurityFilterChain api(HttpSecurity http) throws Exception {

        // Convert claims → GrantedAuthority
        Converter<Jwt, AbstractAuthenticationToken> converter = jwt -> {
            Collection<GrantedAuthority> auths = new ArrayList<>();

            // 1) roles claim → ROLE_ authorities
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                roles.forEach(r -> auths.add(new SimpleGrantedAuthority("ROLE_" + r)));
            }

            // 2) permissions claim (optional) OR look up in DB cache
            List<String> perms = jwt.getClaimAsStringList("permissions");
            if (perms == null) {
                perms = new ArrayList<>(authorizationService.permissionsFor(Long.valueOf(jwt.getSubject())));
            }
            perms.forEach(p -> auths.add(new SimpleGrantedAuthority("PERM_" + p)));

            return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, auths);
        };

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**" // static assets
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}
