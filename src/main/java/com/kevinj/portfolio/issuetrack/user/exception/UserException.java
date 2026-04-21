package com.kevinj.portfolio.issuetrack.user.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class UserException extends BusinessException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
