package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import java.time.LocalDateTime;

public record DilemmaUserSearchCommand(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        String keyword,
        LocalDateTime fromDate,
        LocalDateTime toDate
) {
    public DilemmaUserSearchQuery toQuery() {
        return new DilemmaUserSearchQuery(
                page == null ? 1 : page,
                size == null ? 20 : size,
                sortBy == null ? "createdAt" : sortBy,
                direction == null ? "desc" : direction,
                keyword,
                fromDate,
                toDate
        );
    }
}
