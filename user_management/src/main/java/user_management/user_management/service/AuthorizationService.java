package user_management.user_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import user_management.user_management.entity.Permission;
import user_management.user_management.repository.PermissionRepository;

import java.util.HashSet;
import java.util.List;


/**
 * Central place for resolving and caching user permissions.
 *
 * Cache backend is whatever Spring Cache manager you configured:
 *   • dev  → Caffeine (in‑memory)
 *   • prod → Redis / Hazelcast / Valkey
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userPerms")
public class AuthorizationService {

    private final PermissionRepository permissionRepo;

    /** ------------------------------------------------------------------
     *  Return an immutable list of permission keys, e.g. "invoice:read".
     *  List is cached per userId.
     *  ------------------------------------------------------------------ */
    @Cacheable(key = "#userId")
    public List<String> permissionsFor(Long userId) {
        return permissionRepo.findAllByUserId(userId).stream()
                .map(this::toKey)          // "resource:action"
                .distinct()                // just in case duplicates slip in
                .toList();                 // ArrayList (JSON array ➜ no serialization issues)
    }

    /** Fast allow/deny check (converts to HashSet once per call). */
    public boolean can(Long userId, String resource, String action) {
        List<String> list = permissionsFor(userId);
        return new HashSet<>(list).contains(resource + ':' + action);
    }

    /** Invalidate a single user’s cache entry (call after role / permission change). */
    @CacheEvict(key = "#userId")
    public void invalidate(Long userId) { }

    /** Optional: wipe the whole cache (e.g. nightly job or admin endpoint). */
    @CacheEvict(allEntries = true)
    public void invalidateAll() { }

    /* ---------- helper ---------- */

    private String toKey(Permission p) {
        return p.getResource() + ':' + p.getAction();
    }
}
