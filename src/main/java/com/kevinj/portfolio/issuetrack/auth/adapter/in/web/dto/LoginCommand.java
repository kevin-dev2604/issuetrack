package com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto.UserTokenCommand;

public record LoginCommand(
    String loginId,
    String loginPw,
    UserTokenCommand tokenInfo
) {
}
