package com.kevinj.portfolio.issuetrack.issue.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum IssueErrorCode implements ErrorCode {
    INVALIDATE_INPUT("Input information is invalid"),
    EMPTY_PROCESS("Process have no steps."),
    ISSUE_NOT_FOUND("Issue not found."),
    CANNOT_PROCEED_NEXT_STEP("Cannot proceed next step."),
    DISABLED_STATUS("Request is disabled in this issue status."),
    ;

    final String message;

    IssueErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
