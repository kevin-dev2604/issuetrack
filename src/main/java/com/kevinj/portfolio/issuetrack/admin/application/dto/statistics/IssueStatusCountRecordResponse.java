package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

public record IssueStatusCountRecordResponse(
        String status,
        Long cnt,
        Double ratio
) {
}
