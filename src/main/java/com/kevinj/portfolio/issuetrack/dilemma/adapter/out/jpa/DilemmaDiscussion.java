package com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class DilemmaDiscussion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discussionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dilemma_id", nullable = false)
    private Dilemma dilemma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column()
    private String content;
}
