package com.kevinj.portfolio.issuetrack.issue.exception;

public class CannotProceedNextStepException extends IssueException {
    public CannotProceedNextStepException() {
        super(IssueErrorCode.CANNOT_PROCEED_NEXT_STEP);
    }
}
