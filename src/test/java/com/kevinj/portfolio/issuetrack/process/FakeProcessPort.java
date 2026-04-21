package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.exception.TestConditionFailedException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessSearchQuery;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeProcessPort implements ProcessPort, FakePort {

    private final Map<Long, ProcessDomain> processList = new HashMap<Long, ProcessDomain>();
    private final ProcessAndStepMapper mapper;

    public FakeProcessPort(ProcessAndStepMapper mapper) {
        this.mapper = mapper;
    }

    private Long newProcessId() {
        return (long) processList.size() + 1;
    }

    @Override
    public Page<ProcessInfoResponse> searchProcesses(User user, ProcessSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("depth"), Sort.Order.asc("label"), Sort.Order.desc("createdAt"))
        );

        List<ProcessInfoResponse> content = processList.values()
                .stream()
                .filter(process -> {
                    Boolean result = process.getIsDeleted() == null || process.getIsDeleted().equals(YN.N);
                    if (query.userId() != null) {
                        result &= process.getUserId().equals(query.userId());
                    }
                    if (query.name() != null && !query.name().isBlank()) {
                        result &= process.getName().contains(query.name());
                    }
                    if (query.isActive() != null) {
                        result &= process.getIsActive().equals(query.isActive());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(mapper::toProcessInfoResponse)
                .toList();

        Long total = processList.values()
                .stream()
                .filter(process -> {
                    Boolean result = process.getIsDeleted() == null || process.getIsDeleted().equals(YN.N);
                    if (query.userId() != null) {
                        result &= process.getUserId().equals(query.userId());
                    }
                    if (query.name() != null && !query.name().isBlank()) {
                        result &= process.getName().contains(query.name());
                    }
                    if (query.isActive() != null) {
                        result &= process.getIsActive().equals(query.isActive());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<ProcessDomain> getProcess(User user, Long processId) {
        ProcessDomain process = processList.get(processId);

        if (process == null
                || (process.getIsDeleted() != null && process.getIsDeleted().equals(YN.Y))
                || process.getUserId() == null || !process.getUserId().equals(user.getUserId())
        ) {
            return Optional.empty();
        }

        return Optional.of(process);
    }

    @Override
    public Optional<ProcessDomain> getProcessIncludingDeleted(User user, Long processId) {
        ProcessDomain process = processList.get(processId);

        if (process == null
                || process.getUserId() == null || !process.getUserId().equals(user.getUserId())
        ) {
            return Optional.empty();
        }

        return Optional.of(process);
    }

    @Override
    public Optional<ProcessDomain> getProcessUnscoped(Long processId) {
        ProcessDomain process = processList.get(processId);
        return Optional.ofNullable(process);
    }

    @Override
    public void createProcess(User user, ProcessCreateCommand command) {
        Long processId = newProcessId();
        ProcessDomain process = new ProcessDomain(
                processId,
                user.getUserId(),
                command.name(),
                command.note(),
                command.isActive(),
                YN.N,
                Lists.newArrayList()
        );
        processList.put(processId, process);
    }

    @Override
    public void saveProcess(ProcessDomain process, User user) {
        if (!user.getUserId().equals(process.getUserId())) {
            throw new TestConditionFailedException();
        }

        ProcessDomain before = processList.get(process.getProcessId());
        if (before == null) {
            throw new TestConditionFailedException();
        }

        processList.put(process.getProcessId(), process);
    }

    @Override
    public boolean isUserProcess(User user, Long processId) {
        ProcessDomain process = processList.get(processId);
        return process != null && process.getUserId().equals(user.getUserId());
    }

    @Override
    public Long newId() {
        return (long) processList.size() + 1;
    }

    @Override
    public Long lastId() {
        return processList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
