package com.kevinj.portfolio.issuetrack.auth.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class AuthException extends BusinessException {
    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode);
    }
}
