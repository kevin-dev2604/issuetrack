package com.kevinj.portfolio.issuetrack.process.application.dto.process;

import com.kevinj.portfolio.issuetrack.global.enums.YN;

public record ProcessCreateCommand(
        String name,
        String note,
        YN isActive
) {

    public boolean isValid() {
        if (name == null || name.isBlank()
                || isActive == null) {
            return false;
        }

        return true;
    }
}
