package com.kevinj.portfolio.issuetrack.auth.application.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
