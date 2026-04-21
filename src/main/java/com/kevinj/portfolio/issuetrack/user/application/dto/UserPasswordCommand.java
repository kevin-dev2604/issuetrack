package com.kevinj.portfolio.issuetrack.user.application.dto;

public record UserPasswordCommand(
        String loginPw,
        String newloginPw
) {
}
