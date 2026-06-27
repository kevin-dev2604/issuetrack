package com.kevinj.portfolio.issuetrack.dilemma;

import com.kevinj.portfolio.issuetrack.dilemma.application.port.DilemmaEventProducerPort;

import java.util.List;

public class FakeDilemmaEventPort implements DilemmaEventProducerPort {

    @Override
    public void sendDilemmaOpen(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }

    @Override
    public void sendDilemmaEdited(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }

    @Override
    public void sendDilemmaClosed(List<String> tokens, Long dilemmaId, String dilemmaTitle, Long createdBy) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }

    @Override
    public void sendDiscussionCreated(List<String> tokens, Long dilemmaId, Long dilemmaDiscussionId, String dilemmaTitle, Long createdBy) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }
}
