package com.kevinj.portfolio.issuetrack.auth.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
    USER_NOT_FOUND("Login id not found"),
    PASSWORD_DO_NOT_MATCH("Passwords do not match"),
    REFRESH_TOKEN_INVALID("Refresh token invalid"),
    ;

    final String message;

    AuthErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
