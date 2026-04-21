package com.kevinj.portfolio.issuetrack.user.exception;

public class NoPasswordInputException extends UserException {
    public NoPasswordInputException() {
        super(UserErrorCode.NO_PASSWORD_INPUT);
    }
}
