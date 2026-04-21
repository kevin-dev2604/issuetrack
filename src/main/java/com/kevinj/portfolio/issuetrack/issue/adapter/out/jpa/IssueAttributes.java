package com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Attributes;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class IssueAttributes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attributes_id", nullable = false)
    private Attributes attributes;

    @Column()
    private String value;

}
