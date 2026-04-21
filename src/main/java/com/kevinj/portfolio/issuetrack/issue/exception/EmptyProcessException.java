package com.kevinj.portfolio.issuetrack.issue.exception;

public class EmptyProcessException extends IssueException {
    public EmptyProcessException() {
        super(IssueErrorCode.EMPTY_PROCESS);
    }
}
