package user_management.user_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import user_management.user_management.entity.User;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
public String showDashboard(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String email,
                            Model model) {
    User user = new User();
    user.setUsername(username != null ? username : "TestUser");
    user.setEmail(email != null ? email : "test@example.com");
    model.addAttribute("user", user);
    return "dashboard";
}

}
