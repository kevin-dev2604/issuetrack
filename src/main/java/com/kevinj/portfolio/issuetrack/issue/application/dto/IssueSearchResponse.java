package com.kevinj.portfolio.issuetrack.issue.application.dto;

import java.time.LocalDateTime;

public record IssueSearchResponse(
        Long issueId,
        Long categoryId,
        String categoryLabel,
        Long processId,
        String processName,
        Long currentStepId,
        String currentStepName,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
