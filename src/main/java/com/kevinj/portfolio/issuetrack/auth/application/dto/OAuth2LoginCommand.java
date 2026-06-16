package com.kevinj.portfolio.issuetrack.auth.application.dto;

public record OAuth2LoginCommand(
    String email,
    String name,
    String provider
) {
}
