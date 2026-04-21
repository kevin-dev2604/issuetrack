package com.kevinj.portfolio.issuetrack.issue.application;

import com.kevinj.portfolio.issuetrack.admin.application.port.AttributesManagePort;
import com.kevinj.portfolio.issuetrack.admin.application.port.CategoryManagePort;
import com.kevinj.portfolio.issuetrack.auth.exception.UserNotFoundException;
import com.kevinj.portfolio.issuetrack.issue.application.dto.*;
import com.kevinj.portfolio.issuetrack.issue.application.port.IssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.issue.exception.*;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessPort;
import com.kevinj.portfolio.issuetrack.process.application.port.StepPort;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.process.domain.StepDomain;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.process.exception.step.StepNotFoundException;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class IssueService implements IssueUseCase {

    private final UserPort userPort;
    private final CategoryManagePort categoryManagePort;
    private final AttributesManagePort attributesManagePort;
    private final ProcessPort processPort;
    private final StepPort stepPort;
    private final IssuePort issuePort;

    @Override
    public void createIssue(Long userId, IssueCreateCommand issueCreateCommand) {

        List<Long> attributesIdList = issueCreateCommand.issueAttributes()
                .stream()
                .map(IssueAttributesBasicInfo::attributesId)
                .toList();

        if (!issueCreateCommand.isValid() || !validateIssueInfo(issueCreateCommand.categoryId(), attributesIdList)) {
            throw new InvalidInputException();
        }

        User user = getUser(userId);

        if (!validateProcess(user, issueCreateCommand.processId())) {
            throw new ProcessNotFoundException();
        }

        ProcessDomain process = processPort.getProcess(user, issueCreateCommand.processId()).get();
        StepDomain initialStep = stepPort.getInitialStep(user, process)
                .orElseThrow(EmptyProcessException::new);

        IssueDomain issueDomain = new IssueDomain(
            userId,
            issueCreateCommand.categoryId(),
            issueCreateCommand.processId(),
            initialStep.getStepId(),
            issueCreateCommand.title(),
            issueCreateCommand.details()
        );

        Long issueId = issuePort.createIssue(issueDomain);

        issuePort.createIssueAttributes(issueId, issueCreateCommand.issueAttributes());
    }

    @Override
    public void changeIssueInfo(Long userId, IssueModifyCommand issueModifyCommand) {
        User user = getUser(userId);

        IssueDomain issueDomain = issuePort.getIssue(user, issueModifyCommand.issueId())
                .orElseThrow(IssueNotFoundException::new);

        if (List.of(IssueStatus.EXIT, IssueStatus.DELETED).contains(issueDomain.getStatus())) {
            throw new DisabledStatusException();
        }

        List<Long> attributesIdList = issueModifyCommand.issueAttributes()
                .stream()
                .map(IssueAttributesModifyInfo::attributesId)
                .toList();

        if (!issueModifyCommand.isValid() || !validateIssueInfo(issueModifyCommand.categoryId(), attributesIdList)) {
            throw new InvalidInputException();
        }

        List<IssueAttributesDomain> issueAttributesDomainList = issueModifyCommand.issueAttributes()
                .stream()
                .map(issueAttributesModifyInfo -> new IssueAttributesDomain(
                        issueAttributesModifyInfo.issueAttributesId(),
                        issueDomain.getIssueId(),
                        issueAttributesModifyInfo.attributesId(),
                        issueAttributesModifyInfo.value(),
                        null,
                        null
                    )
                )
                .toList();

        issueDomain.update(issueModifyCommand.categoryId(), issueModifyCommand.title(), issueModifyCommand.details());
        issuePort.saveIssue(issueDomain);

        for (IssueAttributesDomain issueAttributesDomain : issueAttributesDomainList) {
            issuePort.saveIssueAttributes(issueAttributesDomain);
        }
    }

    @Override
    public void proceedIssue(Long userId, Long issueId) {
        User user = getUser(userId);

        IssueDomain issue = issuePort.getIssue(user, issueId)
                .orElseThrow(IssueNotFoundException::new);

        if (!List.of(IssueStatus.HANDLING, IssueStatus.HIDDEN).contains(issue.getStatus())) {
            throw new DisabledStatusException();
        }

        ProcessDomain process = processPort.getProcess(user, issue.getProcessId())
                .orElseThrow(ProcessNotFoundException::new);

        Integer currentStepOrder = stepPort.getStep(user, process, issue.getCurrentStepId())
                .map(StepDomain::getOrder)
                .orElseThrow(StepNotFoundException::new);

        Long nextStepId = stepPort.getNextStep(user, process, currentStepOrder)
                .map(StepDomain::getStepId)
                .orElseThrow(CannotProceedNextStepException::new);

        issue.setProcessStep(nextStepId);

        issuePort.saveIssue(issue);
    }

    @Override
    public void changeStatus(Long userId, Long issueId, IssueStatus issueStatus) {
        User user = getUser(userId);

        IssueDomain issue = issuePort.getIssue(user, issueId)
                .orElseThrow(IssueNotFoundException::new);

        if (List.of(IssueStatus.DILEMMA, IssueStatus.DELETED).contains(issue.getStatus())) {
            throw new DisabledStatusException();
        }

        issue.changeStatus(issueStatus);
        issuePort.saveIssue(issue);
    }

    @Override
    public void changeProcess(Long userId, Long issueId, Long processId) {
        User user = getUser(userId);

        IssueDomain issue = issuePort.getIssue(user, issueId)
                .orElseThrow(IssueNotFoundException::new);

        if (!List.of(IssueStatus.HANDLING, IssueStatus.HIDDEN, IssueStatus.PENDING).contains(issue.getStatus())) {
            throw new DisabledStatusException();
        }

        ProcessDomain process = processPort.getProcess(user, processId)
                .orElseThrow(ProcessNotFoundException::new);

        Long initStepId = stepPort.getInitialStep(user, process)
                .map(StepDomain::getStepId)
                .orElseThrow(EmptyProcessException::new);

        issue.changeProcess(processId);
        issue.setProcessStep(initStepId);
        issuePort.saveIssue(issue);
    }

    @Override
    public void deleteIssue(Long userId, Long issueId) {
        User user = getUser(userId);

        IssueDomain issue = issuePort.getIssue(user, issueId)
            .orElseThrow(IssueNotFoundException::new);

        if (!List.of(IssueStatus.HANDLING, IssueStatus.HIDDEN, IssueStatus.PENDING).contains(issue.getStatus())) {
            throw new DisabledStatusException();
        }

        issue.delete();
        issuePort.saveIssue(issue);
    }

    @Override
    public Page<IssueSearchResponse> searchIssues(Long userId, IssueSearchQuery searchQuery) {
        return issuePort.searchIssues(userId, searchQuery);
    }

    @Override
    public IssueDetailResponse getIssueDetails(Long userId, Long issueId) {
        User user = getUser(userId);

        return issuePort.getIssueDetails(user, issueId);
    }

    private User getUser(Long userId) {
        return userPort.loadById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private boolean validateIssueInfo(Long categoryId, List<Long> attributesIdList) {
        if (attributesIdList != null && !attributesIdList.isEmpty()) {
            for (Long attributeId : attributesIdList) {
                if (attributesManagePort.getAttributes(attributeId).isEmpty()) {
                    return false;
                }
            }
        }

        return categoryManagePort.getCategory(categoryId).isPresent();
    }

    private boolean validateProcess(User user, Long processId) {
        return processPort.getProcess(user, processId).isPresent();
    }

}
