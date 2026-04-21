package com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDilemmaDiscussionRepository extends JpaRepository<DilemmaDiscussion, Long> {
    List<DilemmaDiscussion> findByDilemmaOrderByCreatedAtAsc(Dilemma dilemma);
    Optional<DilemmaDiscussion> findByUserAndDilemmaAndDiscussionId(Users user, Dilemma dilemma, Long discussionId);
}
