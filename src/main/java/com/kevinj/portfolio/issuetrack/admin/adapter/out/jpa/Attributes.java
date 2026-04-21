package com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attributes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributesId;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isUse;

    public Attributes(Long attributesId, String label, YN isUse) {
        this.attributesId = attributesId;
        this.label = label;
        this.isUse = isUse;
    }

}
