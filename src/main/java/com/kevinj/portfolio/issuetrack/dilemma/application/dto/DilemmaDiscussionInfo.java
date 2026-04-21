package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import java.time.LocalDateTime;

public record DilemmaDiscussionInfo(
        Long disussionId,
        Long userId,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
