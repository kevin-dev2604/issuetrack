package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

public record IssueCategoryTreeCountRecordResponse(
        Long groupId,
        String label,
        Integer depth,
        Integer cnt
) {
}
