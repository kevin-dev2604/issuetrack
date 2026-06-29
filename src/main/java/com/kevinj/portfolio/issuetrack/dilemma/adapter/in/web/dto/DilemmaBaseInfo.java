package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

import java.time.LocalDateTime;

public record DilemmaBaseInfo(
        String title,
        String details,
        YN isOpen,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
