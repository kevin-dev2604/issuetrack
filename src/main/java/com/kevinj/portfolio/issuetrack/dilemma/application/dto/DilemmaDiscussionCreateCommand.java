package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

public record DilemmaDiscussionCreateCommand(
        Long dilemmaId,
        String content
) {
}
