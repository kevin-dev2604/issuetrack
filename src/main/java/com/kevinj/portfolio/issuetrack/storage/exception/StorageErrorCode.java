package com.kevinj.portfolio.issuetrack.storage.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    EMPTY_FILE("Empty file input"),
    FILE_NOT_EXISTS("File does not exists"),
    ;

    final String message;

    StorageErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
