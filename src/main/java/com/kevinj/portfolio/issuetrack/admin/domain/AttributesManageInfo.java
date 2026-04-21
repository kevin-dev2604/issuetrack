package com.kevinj.portfolio.issuetrack.admin.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AttributesManageInfo {
    private Long attributesId;
    private String label;
    private YN isUse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(String label, YN isUse) {
        this.label = label;
        this.isUse = isUse;
    }
}
