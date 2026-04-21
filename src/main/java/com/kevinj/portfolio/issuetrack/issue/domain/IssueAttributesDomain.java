package com.kevinj.portfolio.issuetrack.issue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class IssueAttributesDomain {
    private Long id;
    private Long issueId;
    private Long attributesId;
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
