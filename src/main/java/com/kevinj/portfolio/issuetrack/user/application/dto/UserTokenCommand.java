package com.kevinj.portfolio.issuetrack.user.application.dto;

import java.time.LocalDateTime;

public record UserTokenCommand(
        String token,
        String deviceType,
        LocalDateTime lastLoggedInAt
) {
}
