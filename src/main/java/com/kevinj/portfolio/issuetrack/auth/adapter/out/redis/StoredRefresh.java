package com.kevinj.portfolio.issuetrack.auth.adapter.out.redis;

public record StoredRefresh(
        Long userId,
        String tokenHash
) {
}
