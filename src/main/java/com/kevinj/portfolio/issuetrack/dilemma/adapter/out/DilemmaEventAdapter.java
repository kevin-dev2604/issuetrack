package com.kevinj.portfolio.issuetrack.dilemma.adapter.out;

import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEvent;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.event.DilemmaEventType;
import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaEventProducerPort;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DilemmaEventAdapter implements DilemmaEventProducerPort {

    private final KafkaTemplate<String, DilemmaEvent> kafkaTemplate;
    private final ConsumerFactory<String, Object> consumerFactory;
    private final SystemTimeProvider timeProvider;

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
            timeProvider.now()
        );

        kafkaTemplate.send(
            TOPIC,
            event.eventId(),
            event
        );
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
            timeProvider.now()
        );

        kafkaTemplate.send(
            TOPIC,
            event.eventId(),
            event
        );
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
            timeProvider.now()
        );

        kafkaTemplate.send(
            TOPIC,
            event.eventId(),
            event
        );
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
            timeProvider.now()
        );

        kafkaTemplate.send(
            TOPIC,
            event.eventId(),
            event
        );
    }

    private Long getEndOffset() {
        try(Consumer<String, Object> consumer = consumerFactory.createConsumer()) {
            List<TopicPartition> partitions = consumer.partitionsFor(TOPIC)
                .stream()
                .map(info -> new TopicPartition(info.topic(), info.partition()))
                .toList();

            return consumer.endOffsets(partitions).values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
        }
    }
}
