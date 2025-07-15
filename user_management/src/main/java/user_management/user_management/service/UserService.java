package user_management.user_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import user_management.user_management.dto.UserDTO;
import user_management.user_management.entity.Role;
import user_management.user_management.entity.User;
import user_management.user_management.repository.RoleRepository;
import user_management.user_management.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setUpdatedAt(LocalDateTime.now().toString());
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }).collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User updatedUser) {
        // update incoming details if present
        // otherwise keep existing values
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername() != null ? updatedUser.getUsername() : user.getUsername());
            user.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : user.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber() != null ? updatedUser.getPhoneNumber() : user.getPhoneNumber());
            user.setUpdatedAt(LocalDateTime.now().toString() != null ? LocalDateTime.now().toString() : user.getUpdatedAt());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @CacheEvict(value = "permissions")
    public User assignRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));

        if (roles.isEmpty()) {
            throw new RuntimeException("No valid roles found for given IDs.");
        }

        user.getRoles().addAll(roles); // merge with existing roles
        user.setUpdatedAt(LocalDateTime.now().toString());

        return userRepository.save(user);
    }
}
