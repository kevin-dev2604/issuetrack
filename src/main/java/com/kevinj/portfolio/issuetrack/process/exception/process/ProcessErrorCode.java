package com.kevinj.portfolio.issuetrack.process.exception.process;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum ProcessErrorCode implements ErrorCode {
    PROCESS_NOT_FOUND("Process not found"),
    PROCESS_INPUT_INVALID("Process request input is invalid"),
    PROCESS_USED("Process is used"),
    ALREADY_DELETED("Process has been deleted"),
    ;

    final String message;

    ProcessErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
