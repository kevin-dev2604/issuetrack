package com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto;

public record IssueAttributesResponseInfo(
        Long issueAttributesId,
        Long attributesId,
        String label,
        String value
) {
}
