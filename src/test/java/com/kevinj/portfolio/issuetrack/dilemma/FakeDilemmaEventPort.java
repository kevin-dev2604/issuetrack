package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEvent;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEventType;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaEventProducerPort;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeDilemmaEventPort implements DilemmaEventProducerPort {

    private final Map<String, DilemmaEvent> dilemmaEventList = new HashMap<>();
    private final SystemTimeProvider systemTimeProvider = new SystemTimeProvider();

    private final String TOPIC = "dilemma-events";

    @Override
    public void sendDilemmaOpen(Long dilemmaId, String dilemmaTitle, Long createdBy) {
        DilemmaEvent event = new DilemmaEvent(
            String.format("%s-%020d", TOPIC, getEndOffset()),
            DilemmaEventType.DILEMMA_CREATED.getValue(),
            "Dilemma Opened",
            String.format("Dilemma '%s' opened.", dilemmaTitle),
            dilemmaId,
            null,
            createdBy,
            systemTimeProvider.now()
        );

        dilemmaEventList.put(event.eventId(), event);
    }

    @Override
    public void sendDilemmaEdited(Long dilemmaId, String dilemmaTitle, Long createdBy) {
        DilemmaEvent event = new DilemmaEvent(
            String.format("%s-%020d", TOPIC, getEndOffset()),
            DilemmaEventType.DILEMMA_EDITED.getValue(),
            "Dilemma edited",
            String.format("Dilemma '%s' edited.", dilemmaTitle),
            dilemmaId,
            null,
            createdBy,
            systemTimeProvider.now()
        );

        dilemmaEventList.put(event.eventId(), event);
    }

    @Override
    public void sendDilemmaClosed(Long dilemmaId, String dilemmaTitle, Long createdBy) {
        DilemmaEvent event = new DilemmaEvent(
            String.format("%s-%020d", TOPIC, getEndOffset()),
            DilemmaEventType.DILEMMA_CLOSED.getValue(),
            "Dilemma closed",
            String.format("Dilemma '%s' closed.", dilemmaTitle),
            dilemmaId,
            null,
            createdBy,
            systemTimeProvider.now()
        );

        dilemmaEventList.put(event.eventId(), event);
    }

    @Override
    public void sendDiscussionCreated(Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy) {
        DilemmaEvent event = new DilemmaEvent(
            String.format("%s-%020d", TOPIC, getEndOffset()),
            DilemmaEventType.DISCUSSION_CREATED.getValue(),
            "Discussion created",
            String.format("Discussion of Dilemma '%s' created", dilemmaTitle),
            dilemmaId,
            dilemmaDiscussionId,
            createdBy,
            systemTimeProvider.now()
        );

        dilemmaEventList.put(event.eventId(), event);
    }

    private Long getEndOffset() {
        return dilemmaEventList.keySet()
            .stream()
            .filter(key -> key.startsWith(TOPIC))
            .mapToLong(key -> Long.parseLong(key.replace(TOPIC + "-", "")))
            .max()
            .orElse(1L);
    }
}
