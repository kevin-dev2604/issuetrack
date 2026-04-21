package com.kevinj.portfolio.issuetrack.issue.application.dto;

public record IssueAttributesResponseInfo(
        Long issueAttributesId,
        Long attributesId,
        String label,
        String value
) {
}
