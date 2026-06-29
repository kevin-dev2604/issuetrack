package com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto;

public record UserCreateCommand(
        String loginId,
        String loginPw,
        String nickname,
        String email,
        String details,
        String provider
) {
}
