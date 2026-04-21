package com.kevinj.portfolio.issuetrack.process.adapter.out;

import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepInfoResponse;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@NoArgsConstructor
public class ProcessAndStepMapper {

    public StepDomain toStepDomain(Step step) {
        return new StepDomain(
                step.getStepId(),
                step.getProcess().getProcessId(),
                step.getOrder(),
                step.getName(),
                step.getIsActive(),
                step.getIsDeleted()
        );
    }

    public StepInfoResponse toStepInfoResponse(StepDomain stepDomain) {
        return new StepInfoResponse(
                stepDomain.getStepId(),
                stepDomain.getProcessId(),
                stepDomain.getOrder(),
                stepDomain.getName(),
                stepDomain.getIsActive()
        );
    }

    public Step toStepEntity(StepDomain stepDomain, Process process) {
        return new Step(
                stepDomain.getStepId(),
                process,
                stepDomain.getOrder(),
                stepDomain.getName(),
                stepDomain.getIsActive()
        );
    }

    public ProcessDomain toProcessDomain(Process process) {
        return new ProcessDomain(
                process.getProcessId(),
                process.getUser().getUserId(),
                process.getName(),
                process.getNote(),
                process.getIsActive(),
                process.getIsDeleted(),
                process.getSteps()
                        .stream()
                        .map(this::toStepDomain)
                        .toList()
        );
    }

    public ProcessInfoResponse toProcessInfoResponse(Process process) {
        return new ProcessInfoResponse(
                process.getProcessId(),
                process.getName(),
                process.getNote(),
                process.getIsActive(),
                process.getSteps()
                        .stream()
                        .map(this::toStepDomain)
                        .map(this::toStepInfoResponse)
                        .sorted(Comparator.comparing(StepInfoResponse::order))
                        .toList()

        );
    }

    public ProcessInfoResponse toProcessInfoResponse(ProcessDomain processDomain) {
        return new ProcessInfoResponse(
                processDomain.getProcessId(),
                processDomain.getName(),
                processDomain.getNote(),
                processDomain.getIsActive(),
                processDomain.getSteps()
                        .stream()
                        .map(this::toStepInfoResponse)
                        .sorted(Comparator.comparing(StepInfoResponse::order))
                        .toList()

        );
    }

    public Process toProcessEntity(ProcessDomain processDomain, Users user) {
        return new Process(
                processDomain.getProcessId(),
                user,
                processDomain.getName(),
                processDomain.getNote(),
                processDomain.getIsActive()
        );
    }
}
