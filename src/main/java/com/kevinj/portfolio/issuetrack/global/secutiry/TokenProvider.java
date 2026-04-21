package com.kevinj.portfolio.issuetrack.global.secutiry;

public interface TokenProvider {
    String createAccessToken(String loginId, Long userId, String role);
    String createRefreshToken(Long userId);
    Long getUserId(String token);
    boolean validateToken(String token);
    String getLoginId(String accessToken);
    String getRole(String accessToken);
}
