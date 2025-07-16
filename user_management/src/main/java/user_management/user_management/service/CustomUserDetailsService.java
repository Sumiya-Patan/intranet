package user_management.user_management.service;

<<<<<<< HEAD
import user_management.user_management.entity.User;
import user_management.user_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
=======
import user_management.user_management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

<<<<<<< HEAD

import java.util.Collections;

=======
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
<<<<<<< HEAD
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(), // plain text
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
=======
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
    }
}
