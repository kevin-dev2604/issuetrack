package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaDiscussionInfo;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaDiscussionPort;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeDilemmaDiscussionPort implements DilemmaDiscussionPort, FakePort {

    private final Map<Long, DilemmaDiscussionDomain> dilemmaDiscussionDomainList = new HashMap<>();
    private final SystemTimeProvider systemTimeProvider = new SystemTimeProvider();

    @Override
    public void createDilemmaDiscussion(User user, DilemmaDomain dilemma, String content) {
        Long discussionId = newId();


        DilemmaDiscussionDomain newDiscussion = new DilemmaDiscussionDomain(
                discussionId,
                dilemma.getDilemmaId(),
                user.getUserId(),
                content,
                systemTimeProvider.now(),
                systemTimeProvider.now()
        );

        dilemmaDiscussionDomainList.put(discussionId, newDiscussion);
    }

    @Override
    public void editDilemmaDiscussion(DilemmaDiscussionDomain domain) {
        dilemmaDiscussionDomainList.put(domain.getDiscussionId(), domain);
    }

    @Override
    public void deleteDilemmaDiscussion(Long discussionId) {
        dilemmaDiscussionDomainList.remove(discussionId);
    }

    @Override
    public List<DilemmaDiscussionInfo> getDilemmaDiscussionList(Long dilemmaId) {
        return dilemmaDiscussionDomainList.values()
                .stream()
                .filter(domain -> domain.getDilemmaId().equals(dilemmaId))
                .map(domain -> new DilemmaDiscussionInfo(
                        domain.getDiscussionId(),
                        domain.getUserId(),
                        null,
                        domain.getContent(),
                        domain.getCreatedAt(),
                        domain.getUpdatedAt()
                ))
                .toList();
    }

    @Override
    public Optional<DilemmaDiscussionDomain> getDilemmaDiscussion(Long userId, Long dilemmaId, Long discussionId) {
        return dilemmaDiscussionDomainList.values()
                .stream()
                .filter(domain ->
                        domain.getDilemmaId().equals(dilemmaId)
                        && domain.getUserId().equals(userId)
                        && domain.getDiscussionId().equals(discussionId)
                )
                .findFirst();
    }

    @Override
    public Long newId() {
        return (long) dilemmaDiscussionDomainList.size() + 1;
    }

    @Override
    public Long lastId() {
        return dilemmaDiscussionDomainList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
