package com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto;

public record OAuth2LoginCommand(
    String email,
    String name,
    String provider
) {
}
