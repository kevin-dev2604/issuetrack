package com.kevinj.portfolio.issuetrack.process.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Process extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String name;

    @Column()
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isActive;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YN isDeleted;

    @OneToMany(mappedBy = "process")
    private List<Step> steps = new ArrayList<>();

    public Process(Long processId, Users user, String name, String note, YN isActive) {
        this.processId = processId;
        this.user = user;
        this.name = name;
        this.note = note;
        this.isActive = isActive;
        this.isDeleted = YN.N;
    }
}
