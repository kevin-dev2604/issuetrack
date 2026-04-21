package com.kevinj.portfolio.issuetrack.auth.security;

import com.kevinj.portfolio.issuetrack.global.secutiry.TokenProvider;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FakeTokenProvider implements TokenProvider {

    private final Pattern AT_PATTERN = Pattern.compile("^AT:(\\d+):([a-zA-Z0-9._]+):([A-Z]+)$");
    private final Pattern RT_PATTERN = Pattern.compile("^RT:(\\d+):([0-9a-fA-F-]+)$");

    @Override
    public String createAccessToken(String loginId, Long userId, String role) {
        return String.format("AT:%d:%s:%s", userId, loginId, role);
    }

    @Override
    public String createRefreshToken(Long userId) {
        return String.format("RT:%d:%s", userId, UUID.randomUUID().toString());
    }

    @Override
    public Long getUserId(String token) {
        Matcher matcher;

        if (token.startsWith("AT:")) {
            matcher = AT_PATTERN.matcher(token);
        } else if (token.startsWith("RT:")) {
            matcher = RT_PATTERN.matcher(token);
        } else {
            throw new IllegalArgumentException("Invalid token: no access/refresh token");
        }

        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid token: not found userId");
        }
    }

    @Override
    public boolean validateToken(String token) {
        return RT_PATTERN.matcher(token).matches();
    }

    @Override
    public String getLoginId(String accessToken) {
        Matcher matcher = AT_PATTERN.matcher(accessToken);

        if (matcher.matches()) {
            return matcher.group(2);
        } else {
            throw new IllegalArgumentException("Invalid token: not found loginId");
        }
    }

    @Override
    public String getRole(String accessToken) {
        Matcher matcher = AT_PATTERN.matcher(accessToken);

        if (matcher.matches()) {
            return matcher.group(3);
        } else {
            throw new IllegalArgumentException("Invalid token: not found role");
        }
    }
}
