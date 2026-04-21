package com.kevinj.portfolio.issuetrack.auth.adapter.out;

import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.RefreshTokenStore;
import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.StoredRefresh;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;
import com.kevinj.portfolio.issuetrack.auth.application.port.AuthPort;
import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;
import com.kevinj.portfolio.issuetrack.global.secutiry.JwtTokenProvider;
import com.kevinj.portfolio.issuetrack.global.secutiry.TokenHash;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPort {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenStore refreshTokenStore;
    private final PasswordEncodePort passwordEncodePort;

    @Override
    public boolean passwordMatches(String inputPw, String userPw) {
        return passwordEncodePort.matches(inputPw, userPw);
    }

    @Override
    public LoginResponse createLoginResponse(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getLoginId(), user.getUserId(), user.getUserRole().systemRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        refreshTokenStore
                .save(user.getUserId(), TokenHash.sha256(refreshToken), jwtTokenProvider.refreshTtl());

        log.debug("===================================================================");
        log.debug("user id: " + user.getUserId());
        log.debug("access token: " + accessToken);
        log.debug("refresh toekn: " + refreshToken);
        log.debug("===================================================================");

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        boolean validated = true;
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            validated = false;
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<StoredRefresh> stored = refreshTokenStore.get(userId);

        if (stored.isEmpty()) {
            validated = false;

        } else {
            String hash = TokenHash.sha256(refreshToken);
            String tokenHash = stored.get().tokenHash();
            // 해시 비교로 refresh 원문 일치 확인
            if (!hash.equals(tokenHash)) {
                // 재사용/탈취 의심 → 전부 폐기(선택)
                refreshTokenStore.revoke(userId);
                validated = false;
            }
        }

        return validated;
    }

    @Override
    public RefreshResponse createNewToken(User user) {
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());
        refreshTokenStore.save(user.getUserId(), TokenHash.sha256(newRefreshToken), jwtTokenProvider.refreshTtl());

        // 새 access 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getLoginId(), user.getUserId(), user.getUserRole().systemRole());

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public Long revokeToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            // 이미 만료/깨짐이면 "로그아웃 처리 완료"로 보고 204 주는 것도 흔함
            return null;
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        refreshTokenStore.revoke(userId);

        return userId;
    }
}
