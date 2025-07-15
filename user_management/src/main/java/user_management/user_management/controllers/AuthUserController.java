package user_management.user_management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import user_management.user_management.entity.User;
import user_management.user_management.service.UserService;

@Controller
public class AuthUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String email,
                              @RequestParam String password,
                              Model model) {
        User user = userService.authenticate(email, password);
        if (user == null) {
            model.addAttribute("error", "Email or password is incorrect. Please try again.");
        }
        if (user != null) {
            model.addAttribute("user", user);
            return "dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        // Add logout logic if needed (e.g., invalidate session)
        return "redirect:/login";
    }
}
