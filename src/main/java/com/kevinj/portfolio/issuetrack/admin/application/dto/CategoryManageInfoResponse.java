package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

import java.time.LocalDateTime;

public record CategoryManageInfoResponse(
        Long categoryId,
        Long parentCategoryId,
        String label,
        Integer depth,
        YN isUse,
        String parentPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
