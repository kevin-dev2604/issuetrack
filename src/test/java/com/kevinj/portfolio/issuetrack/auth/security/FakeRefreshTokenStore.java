package com.kevinj.portfolio.issuetrack.auth.security;

import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.RefreshTokenStore;
import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.StoredRefresh;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeRefreshTokenStore implements RefreshTokenStore {

    private final Map<Long, StoredRefresh> storage = new HashMap<>();

    @Override
    public void save(Long userId, String refreshTokenHash, Duration ttl) {
        StoredRefresh refresh = new StoredRefresh(userId, refreshTokenHash);
        storage.put(userId, refresh);
    }

    @Override
    public Optional<StoredRefresh> get(Long userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public void revoke(Long userId) {
        storage.remove(userId);
    }
}
