package com.kevinj.portfolio.issuetrack.issue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class IssueDomain {
    private Long issueId;
    private Long userId;
    private Long categoryId;
    private List<IssueAttributesDomain> issueAttributeDomains;
    private Long processId;
    private Long currentStepId;
    private String title;
    private String details;
    private IssueStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IssueDomain(Long userId, Long categoryId, Long processId, Long currentStepId, String title, String details) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.processId = processId;
        this.currentStepId = currentStepId;
        this.title = title;
        this.details = details;
        this.status = IssueStatus.HANDLING;
    }

    public void update(Long categoryId, String title, String details) {
        this.categoryId = categoryId;
        this.title = title;
        this.details = details;
    }

    public void changeProcess(Long processId) {
        this.processId = processId;
    }

    public void setProcessStep(Long stepId) {
        this.currentStepId = stepId;
    }

    public void changeStatus(IssueStatus status) {
        this.status = status;
    }

    public void convertToDilemma() {
        this.status = IssueStatus.DILEMMA;
    }

    public void delete() {
        this.status = IssueStatus.DELETED;
    }

}
