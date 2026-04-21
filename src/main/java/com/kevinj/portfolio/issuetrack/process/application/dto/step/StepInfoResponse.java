package com.kevinj.portfolio.issuetrack.process.application.dto.step;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record StepInfoResponse(
        Long stepId,
        Long processId,
        Integer order,
        String name,
        YN isActive
) {
}
