package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

import java.time.LocalDateTime;

public record DilemmaSearchResponse(
        Long dilemmaId,
        String dilemmaTitle,
        Long issueId,
        String issueTitle,
        YN isOpen,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
