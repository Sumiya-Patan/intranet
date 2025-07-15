package user_management.user_management.controller;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
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
    }
}
