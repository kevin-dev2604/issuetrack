package com.kevinj.portfolio.issuetrack.user.application.dto;

public record UserCreateCommand(
        String loginId,
        String loginPw,
        String nickname,
        String email,
        String details
) {
}
