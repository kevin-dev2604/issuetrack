package com.kevinj.portfolio.issuetrack.user.exception;

public class NoNicknameInputException extends UserException {
    public NoNicknameInputException() {
        super(UserErrorCode.NO_NICKNAME_INPUT);
    }
}
