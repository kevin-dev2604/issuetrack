package com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String role
) {
}
