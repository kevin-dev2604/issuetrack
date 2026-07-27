package com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.Attributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.Category;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.JpaAttributesRepository;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.JpaCategoryRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.IssueMapper;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence.query.IssueQueryRepository;
import com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto.IssueAttributesBasicInfo;
import com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto.IssueDetailResponse;
import com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto.IssueSearchQuery;
import com.kevinj.portfolio.issuetrack.issue.adapter.in.web.dto.IssueSearchResponse;
import com.kevinj.portfolio.issuetrack.issue.application.port.out.IssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.model.enums.IssueStatus;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.JpaProcessRepository;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.JpaStepRepository;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.Step;
import com.kevinj.portfolio.issuetrack.storage.adapter.out.persistence.JpaUploadFileRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
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
    private final JpaProcessRepository jpaProcessRepository;
    private final JpaStepRepository jpaStepRepository;
    private final JpaIssueRepository jpaIssueRepository;
    private final JpaIssueAttributesRepository jpaIssueAttributesRepository;
    private final IssueQueryRepository issueQueryRepository;
    private final JpaIssueFilesRepository jpaIssueFilesRepository;

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final IssueMapper issueMapper;

    @Override
    public Long createIssue(
            IssueDomain issueDomain
    ) {

        Users users = jpaUserRepository.getReferenceById(issueDomain.getUserId());
        Category category = jpaCategoryRepository.getReferenceById(issueDomain.getCategoryId());
        Process process = jpaProcessRepository.getReferenceById(issueDomain.getProcessId());
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
        Process process = jpaProcessRepository.getReferenceById(issueDomain.getProcessId());
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

        IssueDetailResponse result = new IssueDetailResponse(
                issueId,
                issue.getCategory().getCategoryId(),
                categoryMapper.getParentPath(issue.getCategory()),
                issue.getCategory().getLabel(),
                issue.getProcess().getProcessId(),
                issue.getProcess().getName(),
                issue.getCurrentStep().getStepId(),
                issue.getCurrentStep().getName(),
                issue.getTitle(),
                issue.getDetails(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );

        result.setIssueAttributesList(issueQueryRepository.getIssueAttributesDisplayList(issueId));

        return result;
    }

    @Override
    public void saveIssueFiles(Long issueId, List<Long> fileIdList) {
        Issue issue = jpaIssueRepository.getReferenceById(issueId);
        jpaIssueFilesRepository.deleteByIssue_IssueId(issueId);

        for (Long fileId : fileIdList) {
            IssueFiles issueFile = new IssueFiles(issue, fileId);
            jpaIssueFilesRepository.save(issueFile);
        }
    }

    @Override
    public List<Long> getIssueFileIds(Long issueId) {
        Issue issue = jpaIssueRepository.findById(issueId)
            .orElseThrow(() -> new EntityNotFoundException("Issue not found"));

        return issue.getIssueFileList()
            .stream()
            .map(IssueFiles::getFileId)
            .toList();
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
