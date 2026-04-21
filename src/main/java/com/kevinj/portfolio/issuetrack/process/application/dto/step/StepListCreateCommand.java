package com.kevinj.portfolio.issuetrack.process.application.dto.step;

import java.util.List;

public record StepListCreateCommand(
        Long processId,
        List<StepCreateInfo> commandList
) {
}
