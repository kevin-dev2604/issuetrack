package com.kevinj.portfolio.issuetrack.auth.application.dto;

public record LoginCommand(
    String loginId,
    String loginPw
) {
}
