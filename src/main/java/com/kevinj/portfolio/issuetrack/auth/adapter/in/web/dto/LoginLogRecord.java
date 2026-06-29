package com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.user.domain.model.User;

public record LoginLogRecord(
        User user,
        String clientType
) {

    public LoginLogRecord setFailedLog() {
        return new LoginLogRecord(user, clientType);
    }
}
