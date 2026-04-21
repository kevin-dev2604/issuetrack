package com.kevinj.portfolio.issuetrack.admin.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CategoryManageInfo {
    private Long categoryId;
    private Long parentCategoryId;
    private String label;
    private Integer depth;
    private YN isUse;
    private String parentPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void update(Long parentCategoryId, String label, Integer depth, YN isUse) {
        this.parentCategoryId = parentCategoryId;
        this.label = label;
        this.depth = depth;
        this.isUse = isUse;
    }
}
