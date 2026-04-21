package com.kevinj.portfolio.issuetrack.dilemma.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum DilemmaErrorCode implements ErrorCode {
    NOT_FOUND("Dilemma not found"),
    DISCUSSION_NOT_FOUND("Dilemma discussion not found"),
    CLOSED_DILEMMA("Closed dilemma cannot be edited"),
    ;

    final String message;

    DilemmaErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
