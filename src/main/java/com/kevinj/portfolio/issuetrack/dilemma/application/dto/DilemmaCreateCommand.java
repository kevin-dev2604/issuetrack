package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

public record DilemmaCreateCommand(
        Long issueId,
        String title,
        String details
) {
}
