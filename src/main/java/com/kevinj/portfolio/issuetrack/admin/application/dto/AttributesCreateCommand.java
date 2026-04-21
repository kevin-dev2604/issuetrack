package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record AttributesCreateCommand(
        String label,
        YN isUse
) {
}
