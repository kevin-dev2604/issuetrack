package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

public record IssueCreateDateCountCommand(
    StatDateUnit granularity,
    String timezone,
    String from,
    String to
) {
}
