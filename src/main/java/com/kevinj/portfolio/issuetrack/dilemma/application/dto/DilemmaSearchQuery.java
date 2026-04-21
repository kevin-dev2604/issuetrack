package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import java.time.LocalDateTime;

public record DilemmaSearchQuery(
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
}
