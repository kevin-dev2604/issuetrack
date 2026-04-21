package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

public record DilemmaDiscussionDeleteCommand(
        Long discussionId,
        Long dilemmaId
) {
}
