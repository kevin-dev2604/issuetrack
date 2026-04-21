package com.kevinj.portfolio.issuetrack.process.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StepDomain {
    private Long stepId;
    private Long processId;
    private Integer order;
    private String name;
    private YN isActive;
    private YN isDeleted;

    public void update(Integer order, String name, YN isActive) {
        this.order = order;
        this.name = name;
        this.isActive = isActive;
    }

    public void delete() {
        this.isDeleted = YN.Y;
    }
}
