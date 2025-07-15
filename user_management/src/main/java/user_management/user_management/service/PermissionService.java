package user_management.user_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import user_management.user_management.entity.Permission;
import user_management.user_management.repository.PermissionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Cacheable(value = "permissions")
    public List<Permission> getAllPermissions() {
        // simulating a delay to demonstrate caching
        try {
            Thread.sleep(2000); // Simulate a delay of 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(UUID id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deletePermission(UUID id) {
        permissionRepository.deleteById(id);
    }

    public Permission getByName(String name) {
        return permissionRepository.findByName(name).orElse(null);
    }

}
