package user_management.user_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import user_management.user_management.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    
}
