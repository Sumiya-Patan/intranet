package user_management.user_management.controller;

<<<<<<< HEAD
import user_management.user_management.entity.User;
import user_management.user_management.security.JwtUtil;
import user_management.user_management.service.AuthService;
import user_management.user_management.DTOs.AuthRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
=======
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import user_management.user_management.entity.User;
import user_management.user_management.payload.AuthRequest;
import user_management.user_management.payload.AuthResponse;
import user_management.user_management.repository.UserRepository;
import user_management.user_management.security.JwtUtil;
import user_management.user_management.service.CustomUserDetailsService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
<<<<<<< HEAD
            // Authenticate user
            User user = authService.authenticate(request.getUsername(), request.getPassword());

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            // Return the token in response
            return ResponseEntity.ok(new user_management.user_management.DTOs.AuthResponse(token));
        } catch (RuntimeException e) {
            // Return 401 Unauthorized if authentication fails
            return ResponseEntity.status(401).body("Invalid email or password");
        }
=======
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        Set permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails, permissions);

        return ResponseEntity.ok(new AuthResponse(jwt));
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
    }
}
