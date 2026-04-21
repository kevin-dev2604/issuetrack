package com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Dilemma extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dilemmaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(nullable = false)
    private String title;

    @Column()
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isOpen;
}
