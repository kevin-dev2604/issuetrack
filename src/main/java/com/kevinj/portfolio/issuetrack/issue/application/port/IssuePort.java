package com.kevinj.portfolio.issuetrack.issue.application.port;

import com.kevinj.portfolio.issuetrack.issue.application.dto.*;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IssuePort {
    Long createIssue(IssueDomain issueDomain);
    void createIssueAttributes(Long issueId, List<IssueAttributesBasicInfo> issueAttributesBasicInfoList);
    Optional<IssueDomain> getIssue(User user, Long issueId);
    Optional<IssueDomain> getIssueUnscoped(Long issueId);
    List<IssueAttributesDomain> getIssueAttributesList(User user, Long issueId);
    void saveIssue(IssueDomain issueDomain);
    void saveIssueAttributes(IssueAttributesDomain issueAttributesDomain);
    Page<IssueSearchResponse> searchIssues(Long userId, IssueSearchQuery query);
    IssueDetailResponse getIssueDetails(User user, Long issueId);

    // Belows are test-only methods. Do not use in production.
    List<IssueDomain> getAllIssueList();
    List<IssueAttributesDomain> getAllIssueAttributesList();
    
}
