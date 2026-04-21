package com.kevinj.portfolio.issuetrack.user.application.dto;

public record UserUpdateCommand(
        String nickname,
        String email,
        String details
) {
}
