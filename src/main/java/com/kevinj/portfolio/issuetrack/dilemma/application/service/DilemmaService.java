package com.kevinj.portfolio.issuetrack.dilemma.application.service;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.*;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.out.DilemmaDiscussionPort;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.out.DilemmaEventProducerPort;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.out.DilemmaPort;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.in.DilemmaUseCase;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaClosedException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaNotFoundException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DiscussionNotFoundException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.application.port.out.IssuePort;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.model.enums.IssueStatus;
import com.kevinj.portfolio.issuetrack.issue.exception.DisabledStatusException;
import com.kevinj.portfolio.issuetrack.issue.exception.IssueNotFoundException;
import com.kevinj.portfolio.issuetrack.user.application.port.out.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
import com.kevinj.portfolio.issuetrack.user.exception.NotFoundUserException;
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
public class DilemmaService implements DilemmaUseCase {

    private final UserPort userPort;
    private final IssuePort issuePort;
    private final DilemmaPort dilemmaPort;
    private final DilemmaDiscussionPort dilemmaDiscussionPort;
    private final DilemmaEventProducerPort eventProducerPort;

    @Override
    public void openDilemma(Long userId, DilemmaCreateCommand createCommand) {
        User user = getUser(userId);
        IssueDomain issue = issuePort.getIssue(user, createCommand.issueId())
                .orElseThrow(IssueNotFoundException::new);

        if (List.of(IssueStatus.DILEMMA, IssueStatus.EXIT).contains(issue.getStatus())) {
            throw new DisabledStatusException();
        }

        Long dilemmaId = dilemmaPort.createDilemma(createCommand);

        issue.convertToDilemma();
        issuePort.saveIssue(issue);

        List<String> tokens = userPort.findAllUserTokens(user);
        eventProducerPort.sendDilemmaOpen(tokens, dilemmaId, createCommand.title(), userId);
    }

    @Override
    public void editDilemma(Long userId, DilemmaEditCommand editCommand) {
        User user = getUser(userId);
        DilemmaDomain dilemma = dilemmaPort.getDilemma(user, editCommand.dilemmaId())
                .orElseThrow(DilemmaNotFoundException::new);

        if (dilemma.getIsOpen().equals(YN.N)) {
            throw new DilemmaClosedException();
        }

        dilemma.editDilemma(editCommand.title(), editCommand.details());

        dilemmaPort.saveDilemma(dilemma);

        List<String> tokens = userPort.findAllUserTokens(user);
        eventProducerPort.sendDilemmaEdited(tokens, dilemma.getDilemmaId(), editCommand.title(), userId);
    }

    @Override
    public Page<DilemmaSearchResponse> searchMyDilemmaList(Long userId, DilemmaUserSearchQuery userSearchQuery) {
        return dilemmaPort.searchUserDilemma(getUser(userId), userSearchQuery);
    }

    @Override
    public void createDiscussion(Long userId, DilemmaDiscussionCreateCommand createCommand) {
        User user = getUser(userId);
        DilemmaDomain dilemma = dilemmaPort.getDilemma(user, createCommand.dilemmaId())
                .orElseThrow(DilemmaNotFoundException::new);

        Long discussionId = dilemmaDiscussionPort.createDilemmaDiscussion(user, dilemma, createCommand.content());

        List<String> tokens = userPort.findAllUserTokens(user);
        eventProducerPort.sendDiscussionCreated(tokens, dilemma.getDilemmaId(), discussionId, dilemma.getTitle(), userId);
    }

    @Override
    public void editDiscussion(Long userId, DilemmaDiscussionEditCommand editCommand) {
        DilemmaDiscussionDomain discussionDomain = dilemmaDiscussionPort.getDilemmaDiscussion(userId, editCommand.dilemmaId(), editCommand.discussionId())
                .orElseThrow(DiscussionNotFoundException::new);

        discussionDomain.edit(editCommand.content());

        dilemmaDiscussionPort.editDilemmaDiscussion(discussionDomain);
    }

    @Override
    public void deleteDiscussion(Long userId, DilemmaDiscussionDeleteCommand deleteCommand) {
        dilemmaDiscussionPort.getDilemmaDiscussion(userId, deleteCommand.dilemmaId(), deleteCommand.discussionId())
                .orElseThrow(DiscussionNotFoundException::new);

        dilemmaDiscussionPort.deleteDilemmaDiscussion(deleteCommand.dilemmaId());
    }

    @Override
    public void closeDilemma(Long dilemmaId) {
        DilemmaDomain dilemma = dilemmaPort.getDilemmaUnscoped(dilemmaId)
                .orElseThrow(DilemmaNotFoundException::new);

        dilemma.close();
        dilemmaPort.saveDilemma(dilemma);

        IssueDomain issue = issuePort.getIssueUnscoped(dilemma.getIssueId())
            .orElseThrow(IssueNotFoundException::new);

        issue.changeStatus(IssueStatus.PENDING);
        issuePort.saveIssue(issue);

        User user = getUser(issue.getUserId());
        List<String> tokens = userPort.findAllUserTokens(user);
        eventProducerPort.sendDilemmaClosed(tokens, dilemma.getDilemmaId(), dilemma.getTitle(), null);
    }

    @Override
    public Page<DilemmaSearchResponse> searchFullDilemmaList(DilemmaSearchQuery searchQuery) {
        return dilemmaPort.searchFullDilemma(searchQuery);
    }

    @Override
    public DilemmaDetailResponse getDilemmaDetailInfo(Long dilemmaId) {
        DilemmaDomain dilemma = dilemmaPort.getDilemmaUnscoped(dilemmaId)
                .orElseThrow(DilemmaNotFoundException::new);

        User user = dilemmaPort.getDilemmaUser(dilemmaId)
                .orElseThrow(NotFoundUserException::new);

        return new DilemmaDetailResponse(
                issuePort.getIssueDetails(user, dilemma.getIssueId()),
                new DilemmaBaseInfo(
                        dilemma.getTitle(),
                        dilemma.getDetails(),
                        dilemma.getIsOpen(),
                        dilemma.getCreatedAt(),
                        dilemma.getUpdatedAt()
                ),
                dilemmaDiscussionPort.getDilemmaDiscussionList(dilemmaId)
        );
    }

    private User getUser(Long userId) {
        return userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);
    }
}
