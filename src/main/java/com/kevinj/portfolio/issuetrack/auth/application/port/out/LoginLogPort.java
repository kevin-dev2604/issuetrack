package com.kevinj.portfolio.issuetrack.auth.application.port.out;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginLogRecord;

public interface LoginLogPort {
    void recordSuccessLog(LoginLogRecord record);
    void recordFailureLog(LoginLogRecord record);
}
