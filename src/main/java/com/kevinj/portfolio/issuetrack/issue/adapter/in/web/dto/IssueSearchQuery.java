package com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto;

public record IssueSearchQuery(
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
}
