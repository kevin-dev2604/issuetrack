package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

public record DilemmaDiscussionEditCommand(
        Long discussionId,
        Long dilemmaId,
        String content
) {
}
