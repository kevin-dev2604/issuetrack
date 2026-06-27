package com.kevinj.portfolio.issuetrack.auth.application.dto;

import com.kevinj.portfolio.issuetrack.user.application.dto.UserTokenCommand;

public record LoginCommand(
    String loginId,
    String loginPw,
    UserTokenCommand tokenInfo
) {
}
