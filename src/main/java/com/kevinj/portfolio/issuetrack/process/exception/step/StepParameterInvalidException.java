package com.kevinj.portfolio.issuetrack.process.exception.step;

public class StepParameterInvalidException extends StepException {
    public StepParameterInvalidException() {
        super(StepErrorCode.STEP_PARAMETER_INVALID);
    }
}
