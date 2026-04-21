package com.kevinj.portfolio.issuetrack.issue.exception;

public class IssueNotFoundException extends IssueException {
    public IssueNotFoundException() {
        super(IssueErrorCode.ISSUE_NOT_FOUND);
    }
}
