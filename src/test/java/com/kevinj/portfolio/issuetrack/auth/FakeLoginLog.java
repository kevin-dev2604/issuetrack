package com.kevinj.portfolio.issuetrack.auth;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

import java.time.LocalDateTime;

public class FakeLoginLog {
    private Long userId;
    private LocalDateTime loginTime;
    private String clientType;
    private YN isSuccess;

    public FakeLoginLog(Long userId, YN isSuccess, LocalDateTime loginTime, String clientType) {
        this.userId = userId;
        this.isSuccess = isSuccess;
        this.loginTime = loginTime;
        this.clientType = clientType;
    }

    public YN getIsSuccess() {
        return isSuccess;
    }
}
