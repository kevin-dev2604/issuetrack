package com.kevinj.portfolio.issuetrack.admin.application.dto.statistics;

import java.util.List;

public record IssueCreateDateCountResponse(
    StatDateUnit granularity,
    String timezone,
    List<IssueCreateDateCountBucket> buckets
) {
}
