package com.kevinj.portfolio.issuetrack.process.exception.process;

import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;

public abstract class ProcessException extends BusinessException {
    public ProcessException(ProcessErrorCode processErrorCode) {
        super(processErrorCode);
    }
}
