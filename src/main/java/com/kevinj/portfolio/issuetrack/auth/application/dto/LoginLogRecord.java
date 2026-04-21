package com.kevinj.portfolio.issuetrack.auth.application.dto;

import com.kevinj.portfolio.issuetrack.user.domain.User;

public record LoginLogRecord(
        User user,
        String clientType
) {

    public LoginLogRecord setFailedLog() {
        return new LoginLogRecord(user, clientType);
    }
}
