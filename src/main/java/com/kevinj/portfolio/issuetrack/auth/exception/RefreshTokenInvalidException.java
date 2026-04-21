package com.kevinj.portfolio.issuetrack.auth.exception;

public class RefreshTokenInvalidException extends AuthException {
    public RefreshTokenInvalidException() {
        super(AuthErrorCode.REFRESH_TOKEN_INVALID);
    }
}
