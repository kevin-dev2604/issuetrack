package com.kevinj.portfolio.issuetrack.issue.application.dto;

import java.util.List;

public record IssueCreateCommand(
        Long categoryId,
        Long processId,
        List<IssueAttributesBasicInfo> issueAttributes,
        String title,
        String details
) {

    public boolean isValid() {
        if (categoryId == null
                || processId == null
                || title == null || title.isBlank()) {
            return false;
        }
        return true;
    }
}
