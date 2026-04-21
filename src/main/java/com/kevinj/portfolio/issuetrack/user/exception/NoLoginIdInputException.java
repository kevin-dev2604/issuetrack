package com.kevinj.portfolio.issuetrack.user.exception;

public class NoLoginIdInputException extends UserException {
    public NoLoginIdInputException() {
        super(UserErrorCode.NO_LOGIN_ID_INPUT);
    }
}
