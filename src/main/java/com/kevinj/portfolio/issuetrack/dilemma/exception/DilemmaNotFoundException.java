package com.kevinj.portfolio.issuetrack.dilemma.exception;

public class DilemmaNotFoundException extends DilemmaException {
    public DilemmaNotFoundException() {
        super(DilemmaErrorCode.NOT_FOUND);
    }
}
