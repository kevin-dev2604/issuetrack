package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

public record DilemmaDiscussionEditCommand(
        Long discussionId,
        Long dilemmaId,
        String content
) {
}
