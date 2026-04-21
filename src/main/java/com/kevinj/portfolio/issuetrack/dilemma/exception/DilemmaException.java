package com.kevinj.portfolio.issuetrack.dilemma.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class DilemmaException extends BusinessException {
    public DilemmaException(DilemmaErrorCode dilemmaErrorCode) {
        super(dilemmaErrorCode);
    }
}
