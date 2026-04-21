package com.kevinj.portfolio.issuetrack.auth;

import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.RefreshTokenStore;
import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.StoredRefresh;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;
import com.kevinj.portfolio.issuetrack.auth.application.port.AuthPort;
import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;
import com.kevinj.portfolio.issuetrack.global.secutiry.TokenHash;
import com.kevinj.portfolio.issuetrack.global.secutiry.TokenProvider;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.time.Duration;
import java.util.Optional;

public class FakeAuthPort implements AuthPort {

    private final PasswordEncodePort passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenStore refreshTokenStore;

    public FakeAuthPort(PasswordEncodePort passwordEncoder, TokenProvider tokenProvider, RefreshTokenStore fakeRefreshTokenStore) {
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenStore = fakeRefreshTokenStore;
    }

    @Override
    public boolean passwordMatches(String inputPw, String userPw) {
        return passwordEncoder.matches(inputPw, userPw);
    }

    @Override
    public LoginResponse createLoginResponse(User user) {
        String accessToken = tokenProvider.createAccessToken(user.getLoginId(), user.getUserId(), user.getUserRole().systemRole());
        String refreshToken = tokenProvider.createRefreshToken(user.getUserId());

        refreshTokenStore.save(user.getUserId(), refreshToken, Duration.ofDays(14));

        return new  LoginResponse(accessToken, refreshToken);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {

        if (!tokenProvider.validateToken(refreshToken)) {
            return false;
        } else {
            Long userId = tokenProvider.getUserId(refreshToken);
            Optional<StoredRefresh> storedRefresh = refreshTokenStore.get(userId);

            if (storedRefresh.isEmpty() || !storedRefresh.get().tokenHash().equals(refreshToken)) {
                return false;
            }

            return true;
        }
    }

    @Override
    public RefreshResponse createNewToken(User user) {
        String newRefreshToken = tokenProvider.createRefreshToken(user.getUserId());
        refreshTokenStore.save(user.getUserId(), TokenHash.sha256(newRefreshToken), Duration.ofDays(14));

        // 새 access 발급
        String newAccessToken = tokenProvider.createAccessToken(user.getLoginId(), user.getUserId(), user.getUserRole().systemRole());

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public Long revokeToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            // 이미 만료/깨짐이면 "로그아웃 처리 완료"로 보고 204 주는 것도 흔함
            return null;
        }

        Long userId = tokenProvider.getUserId(refreshToken);
        refreshTokenStore.revoke(userId);

        return userId;
    }
}
