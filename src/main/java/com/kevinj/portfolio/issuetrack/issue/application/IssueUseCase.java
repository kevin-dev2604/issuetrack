package com.kevinj.portfolio.issuetrack.issue.application;

import com.kevinj.portfolio.issuetrack.issue.application.dto.*;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import org.springframework.data.domain.Page;

public interface IssueUseCase {
    void createIssue(Long userId, IssueCreateCommand issueCreateCommand);
    void changeIssueInfo(Long userId, IssueModifyCommand issueModifyCommand);
    void proceedIssue(Long userId, Long issueId);
    void changeStatus(Long userId, Long issueId, IssueStatus issueStatus);
    void changeProcess(Long userId, Long issueId, Long processId);
    void deleteIssue(Long userId, Long issueId);
    Page<IssueSearchResponse> searchIssues(Long userId, IssueSearchQuery searchQuery);
    IssueDetailResponse getIssueDetails(Long userId, Long issueId);
}
