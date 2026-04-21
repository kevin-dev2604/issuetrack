package com.kevinj.portfolio.issuetrack.issue.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record IssueDetailResponse(
        Long issueId,
        Long categoryId,
        String parentCategoryPath,
        String categoryLabel,
        List<IssueAttributesResponseInfo> issueAttributesList,
        Long processId,
        String processName,
        Long currentStepId,
        String currentStepName,
        String title,
        String details,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
