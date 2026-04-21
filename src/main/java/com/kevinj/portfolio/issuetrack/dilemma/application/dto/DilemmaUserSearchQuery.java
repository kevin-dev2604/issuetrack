package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import java.time.LocalDateTime;

public record DilemmaUserSearchQuery(
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
}
