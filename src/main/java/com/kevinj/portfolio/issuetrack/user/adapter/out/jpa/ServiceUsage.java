package com.kevinj.portfolio.issuetrack.user.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ServiceUsage extends BaseTimeEntity {
    @EmbeddedId
    private ServiceUsageId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isUse;

    public ServiceUsage(ServiceUsageId id, YN isUse) {
        this.id = id;
        this.isUse = isUse;
    }
}

