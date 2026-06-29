package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

public record DilemmaDiscussionCreateCommand(
        Long dilemmaId,
        String content
) {
}
