package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

import java.math.BigDecimal;

public record IssueDilemmaCategoryOneDepthRatioRecordResponse(
        Long groupId,
        String label,
        BigDecimal dilemmaRatio
) {
}
