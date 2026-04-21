package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.Dilemma;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.DilemmaDiscussion;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.JpaDilemmaDiscussionRepository;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.JpaDilemmaRepository;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaDiscussionInfo;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaDiscussionPort;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DilemmaDiscussionPersistenceAdapter implements DilemmaDiscussionPort {

    private final JpaUserRepository jpaUserRepository;
    private final JpaDilemmaRepository jpaDilemmaRepository;
    private final JpaDilemmaDiscussionRepository jpaDilemmaDiscussionRepository;

    private final UserMapper userMapper;
    private final DilemmaMapper dilemmaMapper;
    private final DilemmaDiscussionMapper dilemmaDiscussionMapper;

    @Override
    public void createDilemmaDiscussion(User user, DilemmaDomain dilemmaDomain, String content) {
        Dilemma dilemma = jpaDilemmaRepository.getReferenceById(dilemmaDomain.getDilemmaId());
        DilemmaDiscussion discussion = new DilemmaDiscussion(
                null,
                dilemma,
                userMapper.toUsersEntity(user),
                content
        );

        jpaDilemmaDiscussionRepository.save(discussion);
    }

    @Override
    public void editDilemmaDiscussion(DilemmaDiscussionDomain domain) {
        Dilemma dilemma = jpaDilemmaRepository.getReferenceById(domain.getDilemmaId());
        Users user = jpaUserRepository.getReferenceById(domain.getUserId());
        jpaDilemmaDiscussionRepository.save(dilemmaDiscussionMapper.toEntity(domain, dilemma, user));
    }

    @Override
    public void deleteDilemmaDiscussion(Long discussionId) {
        jpaDilemmaDiscussionRepository.deleteById(discussionId);
    }

    @Override
    public List<DilemmaDiscussionInfo> getDilemmaDiscussionList(Long dilemmaId) {
        Dilemma dilemma = jpaDilemmaRepository.getReferenceById(dilemmaId);
        return jpaDilemmaDiscussionRepository.findByDilemmaOrderByCreatedAtAsc(dilemma)
                .stream()
                .map(discussion -> new DilemmaDiscussionInfo(
                        discussion.getDiscussionId(),
                        discussion.getUser().getUserId(),
                        discussion.getUser().getNickname(),
                        discussion.getContent(),
                        discussion.getCreatedAt(),
                        discussion.getUpdatedAt()
                ))
                .toList();
    }

    @Override
    public Optional<DilemmaDiscussionDomain> getDilemmaDiscussion(Long userId, Long dilemmaId, Long discussionId) {
        Dilemma dilemma = jpaDilemmaRepository.getReferenceById(dilemmaId);
        Users user = jpaUserRepository.getReferenceById(userId);
        return jpaDilemmaDiscussionRepository.findByUserAndDilemmaAndDiscussionId(user, dilemma,discussionId)
                .map(dilemmaDiscussionMapper::toDomain);
    }
}
