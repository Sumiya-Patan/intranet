package user_management.user_management.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import user_management.user_management.entity.User;
import user_management.user_management.service.UserService;

@Component("securityService")
public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public boolean canAccessUser(Long targetUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_admin"));

        if (isAdmin) return true;

        String currentUsername = auth.getName();
        Optional<User> currentUser = userService.getUserByUsername(currentUsername);
        return currentUser != null && currentUser.get().equals(targetUserId);
    }
}
