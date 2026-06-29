package com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto;

public record UserPasswordCommand(
        String loginPw,
        String newloginPw
) {
}
