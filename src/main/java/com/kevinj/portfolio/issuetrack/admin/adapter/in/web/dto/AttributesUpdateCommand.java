package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record AttributesUpdateCommand(
        Long attributesId,
        String label,
        YN isUse
) {
}
