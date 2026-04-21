package com.kevinj.portfolio.issuetrack.process.application.port;

import com.kevinj.portfolio.issuetrack.process.application.dto.step.StepCreateInfo;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;

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
