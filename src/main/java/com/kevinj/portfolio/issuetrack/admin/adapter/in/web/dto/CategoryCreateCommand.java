package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record CategoryCreateCommand(
        Long parentCategoryId,
        String label,
        YN isUse
) {
}
