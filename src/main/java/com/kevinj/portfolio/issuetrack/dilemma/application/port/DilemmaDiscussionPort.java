package com.kevinj.portfolio.issuetrack.dilemma.application.port;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaDiscussionInfo;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface DilemmaDiscussionPort {
    void createDilemmaDiscussion(User user, DilemmaDomain dilemma, String content);
    void editDilemmaDiscussion(DilemmaDiscussionDomain domain);
    void deleteDilemmaDiscussion(Long discussionId);
    List<DilemmaDiscussionInfo> getDilemmaDiscussionList(Long dilemmaId);
    Optional<DilemmaDiscussionDomain> getDilemmaDiscussion(Long userId, Long dilemmaId, Long discussionId);
}
