package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.Dilemma;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.DilemmaDiscussion;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
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
