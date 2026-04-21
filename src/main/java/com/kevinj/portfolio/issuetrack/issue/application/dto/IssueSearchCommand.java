package com.kevinj.portfolio.issuetrack.issue.application.dto;

public record IssueSearchCommand(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        Long categoryId,
        Long processId,
        String title,
        String details
) {
    public IssueSearchQuery toQuery() {
        return new IssueSearchQuery(
                page == null ? 1 : page,
                size == null ? 20 : size,
                sortBy == null ? "createdAt" : sortBy,
                direction == null ? "desc" : direction,
                categoryId,
                processId,
                title,
                details
        );
    }
}
