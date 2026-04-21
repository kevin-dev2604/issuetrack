package com.kevinj.portfolio.issuetrack.auth.exception;

public class PasswordDoNotMatchException extends AuthException {
    public PasswordDoNotMatchException() {
        super(AuthErrorCode.PASSWORD_DO_NOT_MATCH);
    }
}
