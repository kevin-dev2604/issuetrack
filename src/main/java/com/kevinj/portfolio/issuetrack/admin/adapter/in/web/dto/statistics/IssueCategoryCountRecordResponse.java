package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

public record IssueCategoryCountRecordResponse(
        String label,
        Long cnt,
        Double ratio
) {
}
