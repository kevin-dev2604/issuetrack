package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.exception.TestConditionFailedException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.application.port.StepPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.assertj.core.util.Lists;

import java.util.*;

public class FakeStepPort implements StepPort, FakePort {

    private final Map<Long, StepDomain> stepList = new HashMap<Long, StepDomain>();
    private final ProcessAndStepMapper mapper;

    public FakeStepPort(ProcessAndStepMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Long newId() {
        return (long) stepList.size() + 1;
    }

    @Override
    public Long lastId() {
        return stepList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }

    @Override
    public List<StepDomain> getAllStepList(User user, ProcessDomain process) {
        if (!user.getUserId().equals(process.getUserId())) {
            return Lists.newArrayList();
        }

        return stepList.values()
                .stream()
                .filter(step ->
                        step.getProcessId().equals(process.getProcessId())
                                && !step.getIsDeleted().equals(YN.Y)
                )
                .toList();
    }

    @Override
    public List<StepDomain> getActiveStepList(User user, ProcessDomain process) {
        if (!user.getUserId().equals(process.getUserId())) {
            return Lists.newArrayList();
        }

        return stepList.values()
                .stream()
                .filter(step ->
                        step.getProcessId().equals(process.getProcessId())
                                && !step.getIsDeleted().equals(YN.Y)
                                && step.getIsActive().equals(YN.Y)
                )
                .toList();
    }

    @Override
    public Optional<StepDomain> getStep(User user, ProcessDomain process, Long stepId) {
        if (!user.getUserId().equals(process.getUserId())) {
            return Optional.empty();
        }

        StepDomain step = stepList.get(stepId);
        if (step == null
                || !step.getProcessId().equals(process.getProcessId())
                || step.getIsDeleted().equals(YN.Y)
        ) {
            return Optional.empty();
        }

        return Optional.of(step);
    }

    @Override
    public Optional<StepDomain> getStepUnscoped(Long processId, Long stepId) {
        StepDomain step = stepList.get(stepId);
        if (step == null
                || !step.getProcessId().equals(processId)
        ) {
            return Optional.empty();
        }

        return Optional.of(step);
    }

    @Override
    public Optional<StepDomain> getInitialStep(User user, ProcessDomain process) {
        return stepList.values()
                .stream()
                .filter(step -> step.getProcessId().equals(process.getProcessId()))
                .min(Comparator.comparing(StepDomain::getOrder))
                ;
    }

    @Override
    public Optional<StepDomain> getNextStep(User user, ProcessDomain process, Integer order) {
        return stepList.values()
                .stream()
                .filter(step -> step.getProcessId().equals(process.getProcessId()) && step.getOrder() > order)
                .min(Comparator.comparing(StepDomain::getOrder))
                ;
    }

    @Override
    public void createStep(User user, ProcessDomain process, StepCreateInfo stepCreateInfo) {
        if (!user.getUserId().equals(process.getUserId())) {
            throw new TestConditionFailedException();
        }

        Long stepId = newId();
        StepDomain step = new StepDomain(stepId, process.getProcessId(), stepCreateInfo.order(), stepCreateInfo.name(), YN.Y, YN.N);
        stepList.put(stepId, step);
    }

    @Override
    public void saveStep(User user, ProcessDomain process, StepDomain step) {
        if (!user.getUserId().equals(process.getUserId())) {
            throw new TestConditionFailedException();
        }

        StepDomain before = stepList.get(step.getStepId());
        if (before == null) {
            throw new TestConditionFailedException();
        }

        stepList.put(step.getStepId(), step);
    }
}
