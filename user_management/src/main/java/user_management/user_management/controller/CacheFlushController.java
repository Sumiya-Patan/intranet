package user_management.user_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheFlushController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/auth/flush-cache")
    public ResponseEntity<String> flushRedis() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            return ResponseEntity.ok("✅ Redis cache flushed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("❌ Failed to flush Redis: " + e.getMessage());
        }
    }
}
