package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

public record IssueCategoryDepthCountRecordResponse(
        Long groupId,
        String label,
        Integer cnt
) {
}
