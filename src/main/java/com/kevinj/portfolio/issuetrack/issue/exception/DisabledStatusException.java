package com.kevinj.portfolio.issuetrack.issue.exception;

public class DisabledStatusException extends IssueException {
    public DisabledStatusException() {
        super(IssueErrorCode.DISABLED_STATUS);
    }
}
