package com.kevinj.portfolio.issuetrack.process.exception.process;

public class ProcessAlreadyDeletedException extends ProcessException {
    public ProcessAlreadyDeletedException() {
        super(ProcessErrorCode.ALREADY_DELETED);
    }
}
