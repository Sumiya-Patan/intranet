package user_management.user_management.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import user_management.user_management.entity.User;

import java.util.Collection;

public class AppUserPrincipal implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserPrincipal(User user,
                            Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    /* -------- UserDetails overrides -------- */
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getEmail(); }
    @Override public boolean isAccountNonExpired()  { return user.isActive(); }
    @Override public boolean isAccountNonLocked()   { return user.isActive(); }
    @Override public boolean isCredentialsNonExpired() { return user.isActive(); }
    @Override public boolean isEnabled()            { return user.isActive(); }
}
