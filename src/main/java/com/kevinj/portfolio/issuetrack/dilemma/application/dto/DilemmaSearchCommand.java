package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import java.time.LocalDateTime;

public record DilemmaSearchCommand(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        String nickname,
        Long categoryId,
        Long processId,
        String title,
        String details,
        LocalDateTime createFrom,
        LocalDateTime createTo,
        LocalDateTime updateFrom,
        LocalDateTime updateTo
) {
    public DilemmaSearchQuery toQuery() {
        return new DilemmaSearchQuery(
                page == null ? 1 : page,
                size == null ? 20 : size,
                sortBy == null ? "createdAt" : sortBy,
                direction == null ? "desc" : direction,
                nickname,
                categoryId,
                processId,
                title,
                details,
                createFrom,
                createTo,
                updateFrom,
                updateTo
        );
    }
}
