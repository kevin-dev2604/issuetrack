package com.kevinj.portfolio.issuetrack.dilemma.application.dto;

import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueDetailResponse;

import java.util.List;

public record DilemmaDetailResponse(
        IssueDetailResponse issueInfo,
        DilemmaBaseInfo dilemmaInfo,
        List<DilemmaDiscussionInfo> discussionList
) {
}
