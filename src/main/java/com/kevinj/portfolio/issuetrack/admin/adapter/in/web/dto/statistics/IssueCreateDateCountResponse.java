package com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics;

import java.util.List;

public record IssueCreateDateCountResponse(
    StatDateUnit granularity,
    String timezone,
    List<IssueCreateDateCountBucket> buckets
) {
}
