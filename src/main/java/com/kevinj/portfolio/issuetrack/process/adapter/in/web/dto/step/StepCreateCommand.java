package com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.step;

public record StepCreateCommand(
        Long processId,
        String name,
        Integer order
) {
}
