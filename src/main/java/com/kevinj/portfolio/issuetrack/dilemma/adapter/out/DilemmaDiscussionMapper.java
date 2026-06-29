package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.persistence.Dilemma;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.persistence.DilemmaDiscussion;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DilemmaDiscussionMapper {

    public DilemmaDiscussionDomain toDomain(DilemmaDiscussion discussion) {
        return new DilemmaDiscussionDomain(
                discussion.getDiscussionId(),
                discussion.getDilemma().getDilemmaId(),
                discussion.getUser().getUserId(),
                discussion.getContent(),
                discussion.getCreatedAt(),
                discussion.getUpdatedAt()
        );
    }

    public DilemmaDiscussion toEntity(DilemmaDiscussionDomain domain, Dilemma dilemma, Users user) {
        return new DilemmaDiscussion(
                domain.getDiscussionId(),
                dilemma,
                user,
                domain.getContent()
        );
    }
}
