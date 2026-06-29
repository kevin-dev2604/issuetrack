package com.kevinj.portfolio.issuetrack.dilemma.application.port.out;

import java.util.List;

public interface DilemmaEventProducerPort {
    void sendDilemmaOpen(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDilemmaEdited(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDilemmaClosed(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDiscussionCreated(List<String> tokens, Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy);
}
