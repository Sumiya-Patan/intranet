package user_management.user_management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import user_management.user_management.dto.UserDTO;
import user_management.user_management.entity.User;
import user_management.user_management.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Create user",
           security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<User> createUser(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "User object to create",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                name = "Example User",
                summary = "Sample user creation request",
                value = """
                    {
                      "email": "johndoe@example.com",
                      "password": "password",
                      "username": "johndoe"
                    }
                """
            )
        )
    )    
    @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
        
    }

    @PreAuthorize("hasRole('user')")
    @Operation(summary = "All users list",
           security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

     @PostMapping("/{userId}/assign-roles")
    public ResponseEntity<User> assignRolesToUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        User updatedUser = userService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok(updatedUser);
    }


}
