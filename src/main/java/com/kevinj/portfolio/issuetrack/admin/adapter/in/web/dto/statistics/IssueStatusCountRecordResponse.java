package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

public record IssueStatusCountRecordResponse(
        String status,
        Long cnt,
        Double ratio
) {
}
