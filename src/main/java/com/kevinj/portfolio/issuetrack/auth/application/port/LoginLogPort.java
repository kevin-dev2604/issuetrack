package com.kevinj.portfolio.issuetrack.auth.application.port;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginLogRecord;

public interface LoginLogPort {
    void recordSuccessLog(LoginLogRecord record);
    void recordFailureLog(LoginLogRecord record);
}
