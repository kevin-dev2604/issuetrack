package com.kevinj.portfolio.issuetrack.admin.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class AdminException extends BusinessException {
    public AdminException(AdminErrorCode errorCode) {
        super(errorCode);
    }
}
