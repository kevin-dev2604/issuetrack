package com.kevinj.portfolio.issuetrack.process.exception.step;

public class StepNotFoundException extends StepException {
    public StepNotFoundException() {
        super(StepErrorCode.STEP_NOT_FOUND);
    }
}
