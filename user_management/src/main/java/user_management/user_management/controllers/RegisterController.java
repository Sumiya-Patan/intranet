package user_management.user_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import user_management.user_management.entity.User;
import user_management.user_management.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 Model model) {
        User user = User.builder()
                .username(name)
                .email(email)
                .password(password) // In a real application, ensure to hash the password
                .build(); 
        User created = userService.createUser(user);
        if (created.getClass().equals(User.class)) {
            model.addAttribute("message", "Registration successful! You can now log in.");
        } else {
            model.addAttribute("message", "User already exists.");
        }
        return "register";
    }
}
