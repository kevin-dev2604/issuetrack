package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.Dilemma;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class DilemmaMapper {

    public DilemmaDomain toDomain(Dilemma dilemma) {
        return new DilemmaDomain(
                dilemma.getDilemmaId(),
                dilemma.getIssue().getIssueId(),
                dilemma.getTitle(),
                dilemma.getDetails(),
                dilemma.getIsOpen(),
                dilemma.getCreatedAt(),
                dilemma.getUpdatedAt()
        );
    }

    public Dilemma toEntity(DilemmaDomain domain, Issue issue) {
        return new Dilemma(
                domain.getDilemmaId(),
                issue,
                domain.getTitle(),
                domain.getDetails(),
                domain.getIsOpen()
        );
    }
}
