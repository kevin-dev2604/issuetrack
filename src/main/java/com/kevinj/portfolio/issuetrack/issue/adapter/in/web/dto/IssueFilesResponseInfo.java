package com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto;

public record IssueFilesResponseInfo(
        Long fileId,
        String fileName,
        Long fileSize,
        String url
) {
}
