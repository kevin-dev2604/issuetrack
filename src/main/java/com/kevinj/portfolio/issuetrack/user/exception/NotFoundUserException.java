package com.kevinj.portfolio.issuetrack.user.exception;

public class NotFoundUserException extends UserException {
    public NotFoundUserException() {
        super(UserErrorCode.NOT_FOUND_USER);
    }
}
