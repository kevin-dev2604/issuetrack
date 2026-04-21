package com.kevinj.portfolio.issuetrack.auth.adapter.out.redis;

import java.time.Duration;
import java.util.Optional;

public interface RefreshTokenStore {
    void save(Long userId, String refreshTokenHash, Duration ttl);
    Optional<StoredRefresh> get(Long userId);
    void revoke(Long userId);
}
