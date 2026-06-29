package com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto;

public record UserInfoResponse(
        String loginId,
        String nickname,
        String email,
        String detail
) {
}
