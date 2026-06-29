package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.dilemma.application.port.out.DilemmaEventProducerPort;

import java.util.List;

public class FakeDilemmaEventPort implements DilemmaEventProducerPort {

    @Override
    public void sendDilemmaOpen(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        // no action in fake port
    }

    @Override
    public void sendDilemmaEdited(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        // no action in fake port
    }

    @Override
    public void sendDilemmaClosed(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        // no action in fake port
    }

    @Override
    public void sendDiscussionCreated(List<String> tokens, Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy) {
        // no action in fake port
    }
}
