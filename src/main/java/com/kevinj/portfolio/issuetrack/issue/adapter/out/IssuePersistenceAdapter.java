package com.kevinj.portfolio.issuetrack.issue.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Attributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.JpaAttributesRepository;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.JpaCategoryRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.IssueAttributes;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.JpaIssueAttributesRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.JpaIssueRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.query.IssueQueryRepository;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueAttributesBasicInfo;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueDetailResponse;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchQuery;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchResponse;
import com.kevinj.portfolio.issuetrack.issue.application.port.IssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaProcessRepositiry;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.JpaStepRepository;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IssuePersistenceAdapter implements IssuePort {

    private final JpaUserRepository jpaUserRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaAttributesRepository  jpaAttributesRepository;
    private final JpaProcessRepositiry jpaProcessRepositiry;
    private final JpaStepRepository jpaStepRepository;
    private final JpaIssueRepository jpaIssueRepository;
    private final JpaIssueAttributesRepository jpaIssueAttributesRepository;
    private final IssueQueryRepository issueQueryRepository;

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final IssueMapper issueMapper;

    @Override
    public Long createIssue(
            IssueDomain issueDomain
    ) {

        Users users = jpaUserRepository.getReferenceById(issueDomain.getUserId());
        Category category = jpaCategoryRepository.getReferenceById(issueDomain.getCategoryId());
        Process process = jpaProcessRepositiry.getReferenceById(issueDomain.getProcessId());
        Step initialStepEntity = jpaStepRepository.getReferenceById(issueDomain.getCurrentStepId());

        Issue issue = jpaIssueRepository.save(
            issueMapper.toIssueEntity(
                null,
                users,
                category,
                null,
                process,
                initialStepEntity,
                issueDomain
            )
        );

        return issue.getIssueId();
    }

    @Override
    public void createIssueAttributes(Long issueId, List<IssueAttributesBasicInfo> issueAttributesBasicInfoList) {
        Issue issue = jpaIssueRepository.findById(issueId)
                        .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        for (IssueAttributesBasicInfo issueAttributesBasicInfo : issueAttributesBasicInfoList) {
            Attributes attributes = jpaAttributesRepository.findById(issueAttributesBasicInfo.attributesId())
                    .orElseThrow(() -> new EntityNotFoundException("Attributes not found"));

            jpaIssueAttributesRepository.save(issueMapper.toIssueAttributesEntity(
                    null,
                    issue,
                    attributes,
                    issueAttributesBasicInfo.value()
            ));
        }
    }

    @Override
    public Optional<IssueDomain> getIssue(User user, Long issueId) {
        return jpaIssueRepository.findByIssueIdAndUser(issueId, userMapper.toUsersEntity(user))
                .filter(issue -> !issue.getStatus().equals(IssueStatus.DELETED))
                .map(issueMapper::toIssueDomain);
    }

    @Override
    public Optional<IssueDomain> getIssueUnscoped(Long issueId) {
        return jpaIssueRepository.findById(issueId)
            .filter(issue -> !issue.getStatus().equals(IssueStatus.DELETED))
            .map(issueMapper::toIssueDomain);
    }

    @Override
    public List<IssueAttributesDomain> getIssueAttributesList(User user, Long issueId) {
        Issue issue = jpaIssueRepository.findByIssueIdAndUser(issueId, userMapper.toUsersEntity(user))
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        return issue.getIssueAttributesList()
                .stream()
                .map(issueMapper::toIssueAttributesDomain)
                .toList();
    }

    @Override
    public void saveIssue(IssueDomain issueDomain) {
        Users user = jpaUserRepository.getReferenceById(issueDomain.getUserId());
        Category category = jpaCategoryRepository.getReferenceById(issueDomain.getCategoryId());
        List<IssueAttributes> issueAttributesList = jpaIssueAttributesRepository.findByIssue(
            jpaIssueRepository.getReferenceById(issueDomain.getIssueId())
        );
        Process process = jpaProcessRepositiry.getReferenceById(issueDomain.getProcessId());
        Step currentStep = jpaStepRepository.findById(issueDomain.getCurrentStepId())
            .orElseThrow(() -> new EntityNotFoundException("Step not found"));

        Issue issue = issueMapper.toIssueEntity(
            issueDomain.getIssueId(),
            user,
            category,
            issueAttributesList,
            process,
            currentStep,
            issueDomain
        );

        jpaIssueRepository.save(issue);
    }

    @Override
    public void saveIssueAttributes(IssueAttributesDomain issueAttributesDomain) {
        Issue issue = jpaIssueRepository.getReferenceById(issueAttributesDomain.getIssueId());
        Attributes attributes = jpaAttributesRepository.getReferenceById(issueAttributesDomain.getAttributesId());

        IssueAttributes issueAttributes = issueMapper.toIssueAttributesEntity(
            issueAttributesDomain.getId(),
            issue,
            attributes,
            issueAttributesDomain.getValue()
        );

        jpaIssueAttributesRepository.save(issueAttributes);
    }

    @Override
    public Page<IssueSearchResponse> searchIssues(Long userId, IssueSearchQuery searchQuery) {
        Users user = jpaUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return issueQueryRepository.searchIssues(user, searchQuery);
    }

    @Override
    public IssueDetailResponse getIssueDetails(User user, Long issueId) {
        Issue issue = jpaIssueRepository.findByIssueIdAndUser(issueId, userMapper.toUsersEntity(user))
                .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        return new IssueDetailResponse(
                issueId,
                issue.getCategory().getCategoryId(),
                categoryMapper.getParentPath(issue.getCategory()),
                issue.getCategory().getLabel(),
                issueQueryRepository.getIssueAttributesDisplayList(issueId),
                issue.getProcess().getProcessId(),
                issue.getProcess().getName(),
                issue.getCurrentStep().getStepId(),
                issue.getCurrentStep().getName(),
                issue.getTitle(),
                issue.getDetails(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }

    @Override
    public List<IssueDomain> getAllIssueList() {
        throw new UnsupportedOperationException("This method is for testing only.");
    }

    @Override
    public List<IssueAttributesDomain> getAllIssueAttributesList() {
        throw new UnsupportedOperationException("This method is for testing only.");
    }

}
