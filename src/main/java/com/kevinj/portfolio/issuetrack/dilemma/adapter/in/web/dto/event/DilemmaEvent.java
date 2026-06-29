package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.event;

import java.time.LocalDateTime;

public record DilemmaEvent(
    String eventId,
    String eventType,
    String title,
    String message,
    Long dilemmaId,
    Long discussionId,
    Long createdBy,
    LocalDateTime occurredAt
) {
}
