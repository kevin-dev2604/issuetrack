package com.kevinj.portfolio.issuetrack.issue.application.dto;

public record IssueAttributesModifyInfo(
        Long issueAttributesId,
        Long attributesId,
        String value
) {
}
