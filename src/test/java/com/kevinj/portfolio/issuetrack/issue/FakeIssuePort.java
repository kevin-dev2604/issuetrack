package com.kevinj.portfolio.issuetrack.issue;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.IssueMapper;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueAttributesBasicInfo;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueDetailResponse;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchQuery;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchResponse;
import com.kevinj.portfolio.issuetrack.issue.application.port.IssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.springframework.data.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeIssuePort implements IssuePort, FakePort {

    private final Map<Long, IssueDomain> issueDomainList = new HashMap<>();
    private final Map<Long, IssueAttributesDomain> issueAttributesDomainList = new HashMap<>();
    private final IssueMapper issueMapper = new IssueMapper();

    @Override
    public Long createIssue(IssueDomain issueDomain) {
        Long issueId = newId();
        IssueDomain issue = new IssueDomain(
            issueId,
            issueDomain.getUserId(),
            issueDomain.getCategoryId(),
            issueDomain.getIssueAttributeDomains(),
            issueDomain.getProcessId(),
            issueDomain.getCurrentStepId(),
            issueDomain.getTitle(),
            issueDomain.getDetails(),
            issueDomain.getStatus(),
            null,
            null
        );

        issueDomainList.put(issueId, issue);
        return issueId;
    }

    @Override
    public void createIssueAttributes(Long issueId, List<IssueAttributesBasicInfo> issueAttributesBasicInfoList) {

        for (IssueAttributesBasicInfo issueAttributesBasicInfo : issueAttributesBasicInfoList) {
            Long issueAttributeId = newIssueAttributesId();
            IssueAttributesDomain issueAttributesDomain = new IssueAttributesDomain(
                    issueAttributeId,
                    issueId,
                    issueAttributesBasicInfo.attributesId(),
                    issueAttributesBasicInfo.value(),
                    null,
                    null
            );

            issueAttributesDomainList.put(issueAttributeId, issueAttributesDomain);
        }

    }

    @Override
    public Optional<IssueDomain> getIssue(User user, Long issueId) {
        IssueDomain issue = issueDomainList.get(issueId);

        if (!issue.getUserId().equals(user.getUserId())) {
            return Optional.empty();
        }

        return Optional.of(issue);
    }

    @Override
    public Optional<IssueDomain> getIssueUnscoped(Long issueId) {
        IssueDomain issue = issueDomainList.get(issueId);
        return Optional.of(issue);
    }

    @Override
    public List<IssueAttributesDomain> getIssueAttributesList(User user, Long issueId) {
        return issueAttributesDomainList.values()
                .stream()
                .filter(issueAttributesDomain -> issueAttributesDomain.getIssueId().equals(issueId))
                .toList();
    }

    public Optional<IssueAttributesDomain> getIssueAttributes(Long issueId, Long issueAttributesId) {
        IssueAttributesDomain issueAttributes = issueAttributesDomainList.get(issueAttributesId);

        if (!issueAttributes.getIssueId().equals(issueId)) {
            return Optional.empty();
        }

        return Optional.of(issueAttributes);
    }

    @Override
    public void saveIssue(IssueDomain issueDomain) {
        issueDomainList.put(issueDomain.getIssueId(), issueDomain);
    }

    @Override
    public void saveIssueAttributes(IssueAttributesDomain issueAttributesDomain) {
        issueAttributesDomainList.put(issueAttributesDomain.getIssueId(), issueAttributesDomain);
    }

    @Override
    public Page<IssueSearchResponse> searchIssues(Long userId, IssueSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        List<IssueSearchResponse> content = issueDomainList.values()
                .stream()
                .filter(issue -> {
                    Boolean result = issue.getUserId().equals(userId)
                            && !issue.getStatus().equals(IssueStatus.DELETED);
                    if (query.categoryId() != null) {
                        result &= issue.getCategoryId().equals(query.categoryId());
                    }
                    if (query.processId() != null) {
                        result &= issue.getProcessId().equals(query.processId());
                    }
                    if (query.title() != null && !query.title().isBlank()) {
                        result &= issue.getTitle().contains(query.title());
                    }
                    if (query.details() != null && !query.details().isBlank()) {
                        result &= issue.getDetails().contains(query.details());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(issue -> new IssueSearchResponse(
                        issue.getIssueId(),
                        issue.getCategoryId(),
                        null,
                        issue.getProcessId(),
                        null,
                        issue.getCurrentStepId(),
                        null,
                        issue.getTitle(),
                        issue.getCreatedAt(),
                        issue.getUpdatedAt()
                ))
                .toList();

        Long total = issueDomainList.values()
                .stream()
                .filter(issue -> {
                    Boolean result = issue.getUserId().equals(userId)
                            && !issue.getStatus().equals(IssueStatus.DELETED);
                    if (query.categoryId() != null) {
                        result &= issue.getCategoryId().equals(query.categoryId());
                    }
                    if (query.processId() != null) {
                        result &= issue.getProcessId().equals(query.processId());
                    }
                    if (query.title() != null && !query.title().isBlank()) {
                        result &= issue.getTitle().contains(query.title());
                    }
                    if (query.details() != null && !query.details().isBlank()) {
                        result &= issue.getDetails().contains(query.details());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public IssueDetailResponse getIssueDetails(User user, Long issueId) {
        IssueDomain issue = issueDomainList.get(issueId);

        if (!issue.getUserId().equals(user.getUserId())) {
            return null;
        }

        return new IssueDetailResponse(
                issueId,
                issue.getCategoryId(),
                null,
                null,
                List.of(),
                issue.getProcessId(),
                null,
                issue.getCurrentStepId(),
                null,
                issue.getTitle(),
                issue.getDetails(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }

    @Override
    public List<IssueDomain> getAllIssueList() {
        return issueDomainList.values()
                .stream()
                .toList();
    }

    @Override
    public List<IssueAttributesDomain> getAllIssueAttributesList() {
        return issueAttributesDomainList.values()
                .stream()
                .toList();
    }

    @Override
    public Long newId() {
        return (long) issueDomainList.size() + 1;
    }

    @Override
    public Long lastId() {
        return issueDomainList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }

    public Long newIssueAttributesId() {
        return (long) issueAttributesDomainList.size() + 1;
    }

    public Long lastIssueAttributesId() {
        return issueAttributesDomainList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
