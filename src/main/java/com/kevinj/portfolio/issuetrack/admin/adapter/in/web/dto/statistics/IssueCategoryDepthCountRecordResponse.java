package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

public record IssueCategoryDepthCountRecordResponse(
        Long groupId,
        String label,
        Integer cnt
) {
}
