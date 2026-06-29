package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

public record DilemmaDiscussionDeleteCommand(
        Long discussionId,
        Long dilemmaId
) {
}
