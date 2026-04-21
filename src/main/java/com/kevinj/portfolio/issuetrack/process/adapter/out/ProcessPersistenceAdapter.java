package com.kevinj.portfolio.issuetrack.process.adapter.out;

import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaProcessRepositiry;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.query.ProcessQueryRepository;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessSearchQuery;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProcessPersistenceAdapter implements ProcessPort {

    private final ProcessQueryRepository queryRepository;
    private final JpaProcessRepositiry jpaProcessRepositiry;
    private final UserMapper userMapper;
    private final ProcessAndStepMapper mapper;

    @Override
    public Page<ProcessInfoResponse> searchProcesses(User user, ProcessSearchQuery query) {
        return queryRepository.searchProcess(userMapper.toUsersEntity(user), query);
    }

    @Override
    public Optional<ProcessDomain> getProcess(User user, Long processId) {
        return queryRepository.getProcess(userMapper.toUsersEntity(user), processId)
                .map(mapper::toProcessDomain);
    }

    @Override
    public Optional<ProcessDomain> getProcessIncludingDeleted(User user, Long processId) {
        return jpaProcessRepositiry.findByProcessIdAndUser(processId, userMapper.toUsersEntity(user))
                .map(mapper::toProcessDomain);
    }

    @Override
    public Optional<ProcessDomain> getProcessUnscoped(Long processId) {
        return jpaProcessRepositiry.findById(processId)
                .map(mapper::toProcessDomain);
    }

    @Override
    public void createProcess(User user, ProcessCreateCommand command) {
        Process process = new Process(null, userMapper.toUsersEntity(user), command.name(), command.note(), command.isActive());
        jpaProcessRepositiry.save(process);
    }

    @Override
    public void saveProcess(ProcessDomain process, User user) {
        Process entity = mapper.toProcessEntity(process, userMapper.toUsersEntity(user));
        jpaProcessRepositiry.save(entity);
    }

    @Override
    public boolean isUserProcess(User user, Long processId) {
        Process process = jpaProcessRepositiry.findById(processId)
                .orElseThrow(() -> new EntityNotFoundException("Process not found"));

        return process.getUser().equals(userMapper.toUsersEntity(user));
    }
}
