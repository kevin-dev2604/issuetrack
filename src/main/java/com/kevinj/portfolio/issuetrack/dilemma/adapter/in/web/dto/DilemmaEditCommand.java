package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

public record DilemmaEditCommand(
        Long dilemmaId,
        String title,
        String details
) {
}
