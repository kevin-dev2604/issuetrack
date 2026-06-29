package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto;

import com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto.IssueDetailResponse;

import java.util.List;

public record DilemmaDetailResponse(
        IssueDetailResponse issueInfo,
        DilemmaBaseInfo dilemmaInfo,
        List<DilemmaDiscussionInfo> discussionList
) {
}
