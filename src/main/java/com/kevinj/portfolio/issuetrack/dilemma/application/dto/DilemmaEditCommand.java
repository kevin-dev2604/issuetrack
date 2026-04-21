package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

public record DilemmaEditCommand(
        Long dilemmaId,
        String title,
        String details
) {
}
