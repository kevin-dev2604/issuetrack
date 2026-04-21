package com.kevinj.portfolio.issuetrack.user.exception;

public class DuplicatedEmailException extends UserException {
    public DuplicatedEmailException() {
        super(UserErrorCode.DUPLICATE_EMAIL);
    }
}
