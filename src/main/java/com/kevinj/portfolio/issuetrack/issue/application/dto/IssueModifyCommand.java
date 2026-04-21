package com.kevinj.portfolio.issuetrack.issue.application.dto;

import java.util.List;

public record IssueModifyCommand(
        Long issueId,
        Long categoryId,
        List<IssueAttributesModifyInfo> issueAttributes,
        String title,
        String details
) {

    public boolean isValid() {
        if (categoryId == null
                || title == null || title.isBlank()) {
            return false;
        }
        return true;
    }
}
