package com.kevinj.portfolio.issuetrack.user.exception;

public class NoInputException extends UserException {
    public NoInputException() {
        super(UserErrorCode.NO_INPUT);
    }
}
