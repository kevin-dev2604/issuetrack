package com.kevinj.portfolio.issuetrack.dilemma.application.port.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaDiscussionInfo;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface DilemmaDiscussionPort {
    Long createDilemmaDiscussion(User user, DilemmaDomain dilemma, String content);
    void editDilemmaDiscussion(DilemmaDiscussionDomain domain);
    void deleteDilemmaDiscussion(Long discussionId);
    List<DilemmaDiscussionInfo> getDilemmaDiscussionList(Long dilemmaId);
    Optional<DilemmaDiscussionDomain> getDilemmaDiscussion(Long userId, Long dilemmaId, Long discussionId);
}
