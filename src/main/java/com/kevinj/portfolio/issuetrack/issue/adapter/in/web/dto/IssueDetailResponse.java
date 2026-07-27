package com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class IssueDetailResponse {
    private Long issueId;
    private Long categoryId;
    private String parentCategoryPath;
    private String categoryLabel;
    private Long processId;
    private String processName;
    private Long currentStepId;
    private String currentStepName;
    private String title;
    private String details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Setter
    private List<IssueAttributesResponseInfo> issueAttributesList;

    @Setter
    private List<IssueFilesResponseInfo> issueFilesList;

    public IssueDetailResponse(Long issueId, Long categoryId, String parentCategoryPath, String categoryLabel, Long processId, String processName, Long currentStepId, String currentStepName, String title, String details, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.issueId = issueId;
        this.categoryId = categoryId;
        this.parentCategoryPath = parentCategoryPath;
        this.categoryLabel = categoryLabel;
        this.processId = processId;
        this.processName = processName;
        this.currentStepId = currentStepId;
        this.currentStepName = currentStepName;
        this.title = title;
        this.details = details;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
