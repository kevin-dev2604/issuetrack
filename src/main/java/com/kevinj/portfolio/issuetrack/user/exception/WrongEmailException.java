package com.kevinj.portfolio.issuetrack.user.exception;

public class WrongEmailException extends UserException {
    public WrongEmailException() {
        super(UserErrorCode.WRONG_EMAIL);
    }
}
