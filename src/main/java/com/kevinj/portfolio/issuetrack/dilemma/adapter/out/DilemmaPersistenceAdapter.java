package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.Dilemma;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.JpaDilemmaRepository;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.query.DilemmaQueryRepository;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaCreateCommand;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchResponse;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaUserSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaPort;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.JpaIssueRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DilemmaPersistenceAdapter implements DilemmaPort {

    private final JpaIssueRepository jpaIssueRepository;
    private final JpaDilemmaRepository jpaDilemmaRepository;
    private final DilemmaQueryRepository dilemmaQueryRepository;
    private final UserMapper userMapper;
    private final DilemmaMapper dilemmaMapper;

    @Override
    public void createDilemma(DilemmaCreateCommand createCommand) {
        Issue issue = jpaIssueRepository.getReferenceById(createCommand.issueId());
        Dilemma dilemma = new Dilemma(null,
                issue,
                createCommand.title(),
                createCommand.details(),
                YN.Y
        );

        jpaDilemmaRepository.save(dilemma);
    }

    @Override
    public Optional<DilemmaDomain> getDilemma(User user, Long dilemmaId) {
        Users userEntity = userMapper.toUsersEntity(user);

        return jpaDilemmaRepository.findById(dilemmaId)
                .filter(dilemma1 -> dilemma1.getIssue().getUser().equals(userEntity))
                .map(dilemmaMapper::toDomain);
    }

    @Override
    public Optional<DilemmaDomain> getDilemmaUnscoped(Long dilemmaId) {
        return jpaDilemmaRepository.findById(dilemmaId)
                .map(dilemmaMapper::toDomain);
    }

    @Override
    public void saveDilemma(DilemmaDomain dilemmaDomain) {
        Issue issue = jpaIssueRepository.getReferenceById(dilemmaDomain.getIssueId());
        Dilemma dilemma = dilemmaMapper.toEntity(dilemmaDomain, issue);

        jpaDilemmaRepository.save(dilemma);
    }

    @Override
    public Page<DilemmaSearchResponse> searchUserDilemma(User user, DilemmaUserSearchQuery userSearchQuery) {
        Users userEntity = userMapper.toUsersEntity(user);

        return dilemmaQueryRepository.searchUserDilemma(userEntity, userSearchQuery);
    }

    @Override
    public Page<DilemmaSearchResponse> searchFullDilemma(DilemmaSearchQuery searchQuery) {
        return dilemmaQueryRepository.searchDilemma(searchQuery);
    }

    @Override
    public Optional<User> getDilemmaUser(Long dilemmaId) {
        return jpaDilemmaRepository.findById(dilemmaId)
                .map(Dilemma::getIssue)
                .map(Issue::getUser)
                .map(userMapper::toUserDomain);
    }
}
