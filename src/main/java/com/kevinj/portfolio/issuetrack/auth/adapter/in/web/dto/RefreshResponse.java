package com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {
}
