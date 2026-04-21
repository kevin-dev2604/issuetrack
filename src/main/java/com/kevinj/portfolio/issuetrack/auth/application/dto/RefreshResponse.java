package com.kevinj.portfolio.issuetrack.auth.application.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {
}
