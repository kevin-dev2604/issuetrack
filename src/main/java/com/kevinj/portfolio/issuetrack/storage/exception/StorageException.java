package com.kevinj.portfolio.issuetrack.storage.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class StorageException extends BusinessException {
    public StorageException(StorageErrorCode errorCode) {
        super(errorCode);
    }
}
