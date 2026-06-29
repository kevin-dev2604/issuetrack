package com.kevinj.portfolio.issuetrack.process.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.query.ProcessQueryRepository;
import com.kevinj.portfolio.issuetrack.process.application.port.out.ProcessUsageCheckPort;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
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
