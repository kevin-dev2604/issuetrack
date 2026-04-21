package com.kevinj.portfolio.issuetrack.user.exception;

public class WrongOldPasswordException extends UserException {
    public WrongOldPasswordException() {
        super(UserErrorCode.WRONG_OLD_PASSWORD);
    }
}
