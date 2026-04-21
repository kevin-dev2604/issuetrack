package com.kevinj.portfolio.issuetrack.process.application;

import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.*;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessPort;
import com.kevinj.portfolio.issuetrack.process.application.port.StepPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.process.exception.step.StepNotFoundException;
import com.kevinj.portfolio.issuetrack.process.exception.step.StepParameterInvalidException;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import com.kevinj.portfolio.issuetrack.user.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StepService implements StepUseCase{

    private final UserPort userPort;
    private final ProcessPort processPort;
    private final StepPort stepPort;
    private final ProcessAndStepMapper processAndStepMapper;

    @Override
    public List<StepInfoResponse> getAllStepList(Long userId, Long processId) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, processId);

        return stepPort.getAllStepList(user, process)
                .stream()
                .map(processAndStepMapper::toStepInfoResponse)
                .toList();
    }

    @Override
    public List<StepInfoResponse> getActiveStepList(Long userId, Long processId) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, processId);

        return stepPort.getActiveStepList(user, process)
                .stream()
                .map(processAndStepMapper::toStepInfoResponse)
                .toList();
    }

    @Override
    public StepInfoResponse getStepInfo(Long userId, StepCommand command) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, command.processId());

        return stepPort.getStep(user, process, command.stepId())
                .map(processAndStepMapper::toStepInfoResponse)
                .orElseThrow(StepNotFoundException::new);
    }

    @Override
    public void createStepInfo(Long userId, StepCreateCommand createCommand) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, createCommand.processId());
        StepCreateInfo createInfo = new StepCreateInfo(createCommand.name(), createCommand.order());

        createOneStep(createInfo, user, process);
    }

    @Override
    public void createStepList(Long userId, StepListCreateCommand listCreateCommand) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, listCreateCommand.processId());

        listCreateCommand.commandList()
                .stream()
                .sorted(Comparator.comparing(StepCreateInfo::order))
                .forEach(createInfo -> createOneStep(createInfo, user, process));
    }

    @Override
    public void updateStepInfo(Long userId, StepUpdateCommand updateCommand) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, updateCommand.processId());

        StepDomain step = stepPort.getStep(user, process, updateCommand.stepId())
                .orElseThrow(StepNotFoundException::new);

        StepCreateInfo updateInfo = new StepCreateInfo(updateCommand.name(), updateCommand.order());
        if (!updateInfo.isValid() || updateCommand.isActive() == null) {
            throw new StepParameterInvalidException();
        }

        step.update(updateCommand.order(), updateCommand.name(), updateCommand.isActive());
        stepPort.saveStep(user, process, step);
    }

    @Override
    public void deleteStepInfo(Long userId, StepCommand command) {
        User user = findUser(userId);
        ProcessDomain process = findProcess(user, command.processId());

        StepDomain step = stepPort.getStep(user, process, command.stepId())
                .orElseThrow(StepNotFoundException::new);

        step.delete();
        stepPort.saveStep(user, process, step);
    }

    private User findUser(Long userId) {
        return userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);
    }

    private ProcessDomain findProcess(User user, Long processId) {
        return processPort.getProcess(user, processId)
                .orElseThrow(ProcessNotFoundException::new);
    }

    private void createOneStep(StepCreateInfo createInfo, User user, ProcessDomain process) {
        if (!createInfo.isValid()) {
            throw new StepParameterInvalidException();
        }

        stepPort.createStep(user, process, createInfo);
    }
}
