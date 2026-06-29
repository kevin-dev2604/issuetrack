package com.kevinj.portfolio.issuetrack.dilemma.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.DilemmaDiscussionMapper;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.DilemmaMapper;
import com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.DilemmaDiscussionInfo;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.out.DilemmaDiscussionPort;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDiscussionDomain;
import com.kevinj.portfolio.issuetrack.dilemma.domain.model.DilemmaDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.UserMapper;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
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
    public Long createDilemmaDiscussion(User user, DilemmaDomain dilemmaDomain, String content) {
        Dilemma dilemma = jpaDilemmaRepository.getReferenceById(dilemmaDomain.getDilemmaId());
        DilemmaDiscussion discussion = new DilemmaDiscussion(
                null,
                dilemma,
                userMapper.toUsersEntity(user),
                content
        );

        jpaDilemmaDiscussionRepository.save(discussion);
        return discussion.getDiscussionId();
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
