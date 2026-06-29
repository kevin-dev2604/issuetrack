package com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto;

import java.time.LocalDateTime;

public record UserTokenCommand(
        String token,
        String deviceType,
        LocalDateTime lastLoggedInAt
) {
}
