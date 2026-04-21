package com.kevinj.portfolio.issuetrack.auth;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginLogRecord;
import com.kevinj.portfolio.issuetrack.auth.application.port.LoginLogPort;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;

import java.util.ArrayList;
import java.util.List;

public class FakeLoginLogPort implements LoginLogPort {

    private final List<FakeLoginLog> logList = new ArrayList<>();
    private final SystemTimeProvider timeProvider = new SystemTimeProvider();

    @Override
    public void recordSuccessLog(LoginLogRecord record) {
        logList.add(new FakeLoginLog(record.user().getUserId(), YN.Y, timeProvider.now(), record.clientType()));
    }

    @Override
    public void recordFailureLog(LoginLogRecord record) {
        logList.add(new FakeLoginLog(record.user().getUserId(), YN.N, timeProvider.now(), record.clientType()));
    }

    public FakeLoginLog getLastLog() {
        return logList.isEmpty() ? null : logList.getLast();
    }
}
