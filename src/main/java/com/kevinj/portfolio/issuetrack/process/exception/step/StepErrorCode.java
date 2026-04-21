package com.kevinj.portfolio.issuetrack.process.exception.step;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum StepErrorCode implements ErrorCode {
    STEP_NOT_FOUND("Step not found"),
    STEP_PARAMETER_INVALID("Step parameter is invalid"),
    ;

    final String message;

    StepErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
