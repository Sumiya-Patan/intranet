package user_management.user_management.controller;

import user_management.user_management.dto.RoleDTO;
import user_management.user_management.entity.Role;
import user_management.user_management.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/assign-permissions")
    public ResponseEntity<Role> assignPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody Set<UUID> permissionIds) {
        Role updatedRole = roleService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok(updatedRole);
    }
}
