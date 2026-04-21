package com.kevinj.portfolio.issuetrack.user.exception;

public class NoEmailInputException extends UserException {
    public NoEmailInputException() {
        super(UserErrorCode.NO_EMAIL_INPUT);
    }
}
