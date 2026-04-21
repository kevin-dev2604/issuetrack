package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record CategoryUpdateCommand(
        Long categoryId,
        Long parentCategoryId,
        String label,
        YN isUse
) {
}
