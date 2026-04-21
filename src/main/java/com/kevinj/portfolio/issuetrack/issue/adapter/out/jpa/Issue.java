package com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Issue extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "issue")
    private List<IssueAttributes> issueAttributesList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process", nullable = false)
    private Process process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_step", nullable = false)
    private Step currentStep;

    @Column(nullable = false)
    private String title;

    @Column()
    private String details;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

}
