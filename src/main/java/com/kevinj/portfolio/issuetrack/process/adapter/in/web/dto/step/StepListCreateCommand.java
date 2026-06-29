package com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.step;

import java.util.List;

public record StepListCreateCommand(
        Long processId,
        List<StepCreateInfo> commandList
) {
}
