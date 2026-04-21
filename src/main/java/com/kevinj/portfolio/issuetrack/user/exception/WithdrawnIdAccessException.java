package com.kevinj.portfolio.issuetrack.user.exception;

public class WithdrawnIdAccessException extends UserException {
    public WithdrawnIdAccessException() {
        super(UserErrorCode.WITHDRAWN_ID);
    }
}
