package com.kevinj.portfolio.issuetrack.dilemma.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DilemmaDomain {
    private Long dilemmaId;
    private Long issueId;
    private String title;
    private String details;
    private YN isOpen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void editDilemma(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public void close() {
        this.isOpen = YN.N;
    }
}
