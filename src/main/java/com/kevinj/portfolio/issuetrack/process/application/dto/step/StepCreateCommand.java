package com.kevinj.portfolio.issuetrack.process.application.dto.step;

public record StepCreateCommand(
        Long processId,
        String name,
        Integer order
) {
}
