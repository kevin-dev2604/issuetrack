package com.kevinj.portfolio.issuetrack.auth.adapter.out.redis;

import com.kevinj.portfolio.issuetrack.global.secutiry.TokenHash;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenStore implements RefreshTokenStore {
    private final StringRedisTemplate redis;

    private static String key(Long userId) {
        return "refresh_token:" + userId;
    }

    @Override
    public void save(Long userId, String refreshToken, Duration ttl) {
        String key = key(userId);
        redis.opsForValue().set(key, TokenHash.sha256(refreshToken), ttl);
    }

    @Override
    public Optional<StoredRefresh> get(Long userId) {
        String k = key(userId);
        String tokenHash = (String) redis.opsForHash().get(k, "tokenHash");

        if (tokenHash == null) {
            return Optional.empty();
        }

        return Optional.of(new StoredRefresh(userId, tokenHash));
    }

    @Override
    public void revoke(Long userId) {
        redis.delete(key(userId));
    }
}
