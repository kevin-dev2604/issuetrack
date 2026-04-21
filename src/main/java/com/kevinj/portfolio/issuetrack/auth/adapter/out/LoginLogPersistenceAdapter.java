package com.kevinj.portfolio.issuetrack.auth.adapter.out;

import com.kevinj.portfolio.issuetrack.auth.adapter.out.jpa.JpaLoginLogRepository;
import com.kevinj.portfolio.issuetrack.auth.adapter.out.jpa.LoginLog;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginLogRecord;
import com.kevinj.portfolio.issuetrack.auth.application.port.LoginLogPort;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.time.TimeProvider;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginLogPersistenceAdapter implements LoginLogPort {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JpaLoginLogRepository jpaLoginLogRepository;
    private final TimeProvider timeProvider;

    @Override
    public void recordSuccessLog(LoginLogRecord record) {
        Users users = userMapper.toUsersEntity(record.user());
        LoginLog loginLog = new LoginLog(users, YN.Y, timeProvider.now(), record.clientType());

        jpaLoginLogRepository.save(loginLog);
    }

    @Override
    public void recordFailureLog(LoginLogRecord record) {

        userService.addLoginFailCnt(record.user().getUserId());

        Users users = userMapper.toUsersEntity(record.user());
        LoginLog loginLog = new LoginLog(users, YN.N, timeProvider.now(), record.clientType());

        jpaLoginLogRepository.save(loginLog);
    }
}
