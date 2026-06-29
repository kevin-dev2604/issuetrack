package com.kevinj.portfolio.issuetrack.process.application.port.in;

import com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.step.*;

import java.util.List;

public interface StepUseCase {

    List<StepInfoResponse> getAllStepList(Long userId, Long processId);
    List<StepInfoResponse> getActiveStepList(Long userId, Long processId);
    StepInfoResponse getStepInfo(Long userId, StepCommand command);
    void createStepInfo(Long userId, StepCreateCommand createCommand);
    void createStepList(Long userId, StepListCreateCommand listCreateCommand);
    void updateStepInfo(Long userId, StepUpdateCommand updateCommand);
    void deleteStepInfo(Long userId, StepCommand command);
}
