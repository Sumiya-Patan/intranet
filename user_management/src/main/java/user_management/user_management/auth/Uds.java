package user_management.user_management.auth;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import user_management.user_management.entity.Role;
import user_management.user_management.entity.User;
import user_management.user_management.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class Uds implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User u = repo.findByEmail(username)
                     .orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> auths = u.getRoles().stream()
                .map(Role::getName)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        return new AppUserPrincipal(u, auths);
    }
}
