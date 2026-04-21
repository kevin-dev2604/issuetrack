package com.kevinj.portfolio.issuetrack.global.exception.business;

import lombok.Getter;

public abstract class BusinessException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }
}
