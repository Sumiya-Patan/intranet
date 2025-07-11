package user_management.user_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import user_management.user_management.dto.RoleDTO;
import user_management.user_management.dto.UserSummaryDTO;
import user_management.user_management.entity.Permission;
import user_management.user_management.entity.Role;
import user_management.user_management.repository.PermissionRepository;
import user_management.user_management.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> {
            RoleDTO dto = new RoleDTO();
            dto.setId(role.getId());
            dto.setName(role.getName());
            dto.setDescription(role.getDescription());

            dto.setPermissions(
                    role.getPermissions().stream()
                            .map(Permission::getName)
                            .collect(Collectors.toSet()));

            dto.setUsers(
                    role.getUsers().stream().map(user -> {
                        UserSummaryDTO u = new UserSummaryDTO();
                        u.setUserId(user.getUserId());
                        u.setUsername(user.getUsername());
                        u.setEmail(user.getEmail());
                        return u;
                    }).collect(Collectors.toSet()));

            return dto;
        }).collect(Collectors.toList());
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role updateRole(Long id, Role updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setName(updatedRole.getName());
            role.setDescription(updatedRole.getDescription());
            return roleRepository.save(role);
        }).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Role assignPermissionsToRole(Long roleId, Set<UUID> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));

        if (permissions.isEmpty()) {
            throw new RuntimeException("No valid permissions found for given IDs.");
        }

        role.getPermissions().addAll(permissions); // merge with existing permissions
        return roleRepository.save(role);
    }
}
