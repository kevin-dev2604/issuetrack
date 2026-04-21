package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

public record IssueCreateDateCountCommand(
    StatDateUnit granularity,
    String timezone,
    String from,
    String to
) {
}
