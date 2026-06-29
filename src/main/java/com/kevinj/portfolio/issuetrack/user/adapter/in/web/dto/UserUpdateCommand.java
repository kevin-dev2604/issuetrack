package com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto;

public record UserUpdateCommand(
        String nickname,
        String email,
        String details
) {
}
