package com.kevinj.portfolio.issuetrack.process.application.dto.process;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record ProcessSearchCommand(
        // paging parameters
        Integer page,
        Integer size,
        String sortBy,
        String direction,

        // search condition parameters
        String name,
        YN isActive
) {
    public ProcessSearchQuery toQuery(Long userId) {
        return new ProcessSearchQuery(
                page == null ? 1 : page,
                size == null ? 20 : size,
                sortBy == null ? "createdAt" : sortBy,
                direction == null ? "desc" : direction,
                userId,
                name,
                isActive
        );
    }
}
