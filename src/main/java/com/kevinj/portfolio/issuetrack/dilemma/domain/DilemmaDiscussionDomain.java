package com.kevinj.portfolio.issuetrack.dilemma.domain;

import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DilemmaDiscussionDomain {
    private Long discussionId;
    private Long dilemmaId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void edit(String content) {
        this.content = content;
        this.updatedAt = new SystemTimeProvider().now();
    }
}
