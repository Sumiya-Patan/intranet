package user_management.user_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import user_management.user_management.entity.Permission;
import user_management.user_management.repository.PermissionRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Central place for resolving and caching user‑permissions.
 *
 * Cache backend is whatever Spring Cache manager you configured:
 *  • dev  → Caffeine (in‑memory)
 *  • prod → Redis / Hazelcast, etc.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "userPerms")          // all annotations share this name
public class AuthorizationService {

    private final PermissionRepository permissionRepo;

    /** ------------------------------------------------------------------
     *  Return the full set of permission keys for a user, e.g. "invoice:read".
     *  Results are cached by userId.
     *  ------------------------------------------------------------------ */
    @Cacheable(key = "#userId")
    public Set<String> permissionsFor(Long userId) {
        return permissionRepo.findAllByUserId(userId).stream()
                .map(this::toKey)               // resource + ":" + action
                .collect(Collectors.toUnmodifiableSet());
    }

    /** Convenience helper for a quick allow/deny check (no DB hit if cached). */
    public boolean can(Long userId, String resource, String action) {
        return permissionsFor(userId).contains(resource + ':' + action);
    }

    /** Invalidate a single user’s cache entry after role / permission change. */
    @CacheEvict(key = "#userId")
    public void invalidate(Long userId) { /* nothing else needed */ }

    /** Optional: wipe the whole cache (e.g. nightly job) */
    @CacheEvict(allEntries = true)
    public void invalidateAll() { /* no‑op */ }

    /* ---------- private helpers ---------- */

    private String toKey(Permission p) {
        return p.getResource() + ':' + p.getAction();
    }
}

