package com.kevinj.portfolio.issuetrack.dilemma.application.port;

public interface DilemmaEventProducerPort {
    void sendDilemmaOpen(Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDilemmaEdited(Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDilemmaClosed(Long dilemmaId, String dilemmaTitle, Long createdBy);
    void sendDiscussionCreated(Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy);
}
