package com.kevinj.portfolio.issuetrack.issue.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class IssueException extends BusinessException {
    public IssueException(IssueErrorCode issueErrorCode) {
        super(issueErrorCode);
    }
}
