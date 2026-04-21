package com.kevinj.portfolio.issuetrack.process.application;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.*;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessPort;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessUsageCheckPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessAlreadyDeletedException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessInputInvalidException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import com.kevinj.portfolio.issuetrack.user.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessService implements ProcessUseCase{

    private final ProcessAndStepMapper processAndStepMapper;
    private final ProcessPort processPort;
    private final ProcessUsageCheckPort processUsageCheckPort;
    private final UserPort userPort;

    @Override
    public Page<ProcessInfoResponse> searchProcesses(ProcessSearchQuery query) {
        return processPort.searchProcesses(findUserDomain(query.userId()), query);
    }

    @Override
    public ProcessInfoResponse getProcessInfo(Long userId, Long processId) {
        return processPort.getProcess(findUserDomain(userId), processId)
                .map(processAndStepMapper::toProcessInfoResponse)
                .orElseThrow(ProcessNotFoundException::new);
    }

    @Override
    public void createProcessInfo(Long userId, ProcessCreateCommand createCommand) {
        if (!createCommand.isValid()) {
            throw new ProcessInputInvalidException();
        }
        processPort.createProcess(findUserDomain(userId), createCommand);
    }

    @Override
    public void updateProcessInfo(Long userId, ProcessUpdateCommand updateCommand) {
        if (!updateCommand.isValid()) {
            throw new ProcessInputInvalidException();
        }

        User user = findUserDomain(userId);

        ProcessDomain process = processPort.getProcess(user, updateCommand.processId())
                .orElseThrow(ProcessNotFoundException::new);

        process.update(updateCommand.name(), updateCommand.note(), updateCommand.isActive());

        processPort.saveProcess(process, user);
    }

    @Override
    public ProcessDeleteResponse deleteProcessInfo(Long userId, Long processId) {
        User user = findUserDomain(userId);

        ProcessDomain process = processPort.getProcessIncludingDeleted(user, processId)
                .orElseThrow(ProcessNotFoundException::new);

        if (process.getIsDeleted().equals(YN.Y)) {
            throw new ProcessAlreadyDeletedException();

        } else if (processUsageCheckPort.isProcessUsing(user, processId)) {
            process.update(process.getName(), process.getNote(), YN.N);
            processPort.saveProcess(process, user);

            return ProcessDeleteResponse.deActivated();

        } else {
            process.delete();
            processPort.saveProcess(process, user);
            return ProcessDeleteResponse.deleted();
        }
    }

    private User findUserDomain(Long userId) {
        return userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);
    }
}
