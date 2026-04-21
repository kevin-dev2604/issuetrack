package com.kevinj.portfolio.issuetrack.admin.application.dto;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record AttributesSearchCommand(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        String label,
        YN isUse
) {
    public AttributesSearchQuery toQuery() {
        return new AttributesSearchQuery(
                page == null ? 1 : page,
                size == null ? 20 : size,
                sortBy == null ? "createdAt" : sortBy,
                direction == null ? "desc" : direction,
                label,
                isUse
        );
    }
}
