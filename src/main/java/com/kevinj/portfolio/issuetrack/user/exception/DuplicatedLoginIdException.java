package com.kevinj.portfolio.issuetrack.user.exception;

public class DuplicatedLoginIdException extends UserException {
    public DuplicatedLoginIdException() {
        super(UserErrorCode.DUPLICATE_LOGIN_ID);
    }
}
