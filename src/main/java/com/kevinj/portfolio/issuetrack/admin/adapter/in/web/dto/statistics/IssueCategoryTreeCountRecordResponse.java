package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

public record IssueCategoryTreeCountRecordResponse(
        Long groupId,
        String label,
        Integer depth,
        Integer cnt
) {
}
