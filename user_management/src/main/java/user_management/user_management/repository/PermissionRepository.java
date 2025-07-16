package user_management.user_management.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import user_management.user_management.entity.Permission;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(String name);
    
    @Query("""
    select p
    from   User u
    join   u.roles r
    join   r.permissions p
    where  u.userId = :userId
""")
Set<Permission> findAllByUserId(Long userId);

}
