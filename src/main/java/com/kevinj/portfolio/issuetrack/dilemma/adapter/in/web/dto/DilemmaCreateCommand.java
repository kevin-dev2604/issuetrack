package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

public record DilemmaCreateCommand(
        Long issueId,
        String title,
        String details
) {
}
