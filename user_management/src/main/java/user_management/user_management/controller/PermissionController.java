package user_management.user_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import user_management.user_management.entity.Permission;
import user_management.user_management.service.PermissionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable UUID id) {
        return permissionService.getPermissionById(id);
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
    }
}

