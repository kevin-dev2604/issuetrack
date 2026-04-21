package com.kevinj.portfolio.issuetrack.process.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProcessDomain {
    private Long processId;
    private Long userId;
    private String name;
    private String note;
    private YN isActive;
    private YN isDeleted;
    private List<StepDomain> steps;

    public void update(String name, String note, YN isActive) {
        this.name = name;
        this.note = note;
        this.isActive = isActive;
    }

    public void delete() {
        this.isDeleted = YN.Y;
    }
}
