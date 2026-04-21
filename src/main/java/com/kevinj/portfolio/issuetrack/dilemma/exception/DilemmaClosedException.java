package com.kevinj.portfolio.issuetrack.dilemma.exception;

public class DilemmaClosedException extends DilemmaException {
    public DilemmaClosedException() {
        super(DilemmaErrorCode.CLOSED_DILEMMA);
    }
}
