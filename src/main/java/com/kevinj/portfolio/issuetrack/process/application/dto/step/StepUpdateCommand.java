package com.kevinj.portfolio.issuetrack.process.application.dto.step;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record StepUpdateCommand(
        Long stepId,
        Long processId,
        String name,
        Integer order,
        YN isActive
) {
}
