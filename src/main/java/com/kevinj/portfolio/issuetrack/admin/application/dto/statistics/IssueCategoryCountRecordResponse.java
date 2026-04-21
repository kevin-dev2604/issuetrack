package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

public record IssueCategoryCountRecordResponse(
        String label,
        Long cnt,
        Double ratio
) {
}
