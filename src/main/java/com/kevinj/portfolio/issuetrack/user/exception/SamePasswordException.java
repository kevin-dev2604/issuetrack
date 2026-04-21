package com.kevinj.portfolio.issuetrack.user.exception;

public class SamePasswordException extends UserException {
    public SamePasswordException() {
        super(UserErrorCode.SAME_PASSWORD);
    }
}
