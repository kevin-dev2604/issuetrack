package com.kevinj.portfolio.issuetrack.process.exception.step;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class StepException extends BusinessException {
    public StepException(StepErrorCode errorCode) {
        super(errorCode);
    }
}
