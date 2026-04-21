package com.kevinj.portfolio.issuetrack.process.application.dto.process;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record ProcessSearchQuery(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        Long userId,
        String name,
        YN isActive
) {
}
