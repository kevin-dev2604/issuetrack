package com.kevinj.portfolio.issuetrack.process.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Step extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @Column(name = "sort_order", nullable = false)
    private Integer order;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isActive;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YN isDeleted;

    public Step(Long stepId, Process process, Integer order, String name, YN isActive) {
        this.stepId = stepId;
        this.process = process;
        this.order = order;
        this.name = name;
        this.isActive = isActive;
        this.isDeleted = YN.N;
    }
}
