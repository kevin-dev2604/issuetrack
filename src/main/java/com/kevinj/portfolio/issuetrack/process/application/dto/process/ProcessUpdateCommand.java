package com.kevinj.portfolio.issuetrack.process.application.dto.process;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record ProcessUpdateCommand(
        Long processId,
        String name,
        String note,
        YN isActive
) {

    public boolean isValid() {
        if (processId == null || processId < 1L
                || name == null || name.isBlank()
                || isActive == null) {
            return false;
        }

        return true;
    }
}
