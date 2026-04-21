package com.kevinj.portfolio.issuetrack.process.adapter.out;

import com.kevinj.portfolio.issuetrack.process.adapter.out.query.ProcessQueryRepository;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessUsageCheckPort;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessUsageCheckAdapter implements ProcessUsageCheckPort {

    private final ProcessQueryRepository queryRepository;
    private final UserMapper userMapper;

    @Override
    public boolean isProcessUsing(User user, Long processId) {
        return queryRepository.isProcessUsing(userMapper.toUsersEntity(user), processId);
    }
}
