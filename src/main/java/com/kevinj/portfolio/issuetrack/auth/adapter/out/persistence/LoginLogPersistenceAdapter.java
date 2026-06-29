package com.kevinj.portfolio.issuetrack.auth.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginLogRecord;
import com.kevinj.portfolio.issuetrack.auth.application.port.out.LoginLogPort;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.time.TimeProvider;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import com.kevinj.portfolio.issuetrack.user.application.service.UserService;
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
