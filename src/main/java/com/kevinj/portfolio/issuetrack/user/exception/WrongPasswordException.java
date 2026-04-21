package com.kevinj.portfolio.issuetrack.user.exception;

public class WrongPasswordException extends UserException {
    public WrongPasswordException() {
        super(UserErrorCode.WRONG_PASSWORD);
    }
}
