package com.kevinj.portfolio.issuetrack.notifications.application.port;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEvent;

public interface EventConsumerPort {
    void consumeDilemma(DilemmaEvent event);
    void consumeDiscussion(DilemmaEvent event);
}
