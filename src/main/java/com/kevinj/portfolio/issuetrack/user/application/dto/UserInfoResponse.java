package com.kevinj.portfolio.issuetrack.user.application.dto;

public record UserInfoResponse(
        String loginId,
        String nickname,
        String email,
        String detail
) {
}
