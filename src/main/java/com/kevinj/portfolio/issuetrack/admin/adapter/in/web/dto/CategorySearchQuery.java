package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record CategorySearchQuery(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        String label,
        Integer depth,
        YN isUse
) {
}
