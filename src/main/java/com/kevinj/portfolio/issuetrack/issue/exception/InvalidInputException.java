package com.kevinj.portfolio.issuetrack.issue.exception;

public class InvalidInputException extends IssueException {
    public InvalidInputException() {
        super(IssueErrorCode.INVALIDATE_INPUT);
    }
}
