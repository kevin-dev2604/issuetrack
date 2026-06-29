package com.kevinj.portfolio.issuetrack.process.application.port.out;

import com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.domain.model.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.model.StepDomain;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface StepPort {

    List<StepDomain> getAllStepList(User user, ProcessDomain process);
    List<StepDomain> getActiveStepList(User user, ProcessDomain process);

    // Search by User/Process/Step ID (excluding Deletion Check)
    Optional<StepDomain> getStep(User user, ProcessDomain process, Long stepId);
    // Search by Process/Step ID only, excluding user and deletion status
    Optional<StepDomain> getStepUnscoped(Long processId, Long stepId);

    Optional<StepDomain> getInitialStep(User user, ProcessDomain process);
    Optional<StepDomain> getNextStep(User user, ProcessDomain process, Integer order);

    void createStep(User user, ProcessDomain process, StepCreateInfo stepCreateInfo);
    void saveStep(User user, ProcessDomain process, StepDomain step);
}
