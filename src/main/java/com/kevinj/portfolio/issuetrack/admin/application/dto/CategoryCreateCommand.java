package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record CategoryCreateCommand(
        Long parentCategoryId,
        String label,
        YN isUse
) {
}
