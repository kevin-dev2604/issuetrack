package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

import java.time.LocalDateTime;

public record AttributesManageInfoResponse(
        Long attributesId,
        String label,
        YN isUse,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
