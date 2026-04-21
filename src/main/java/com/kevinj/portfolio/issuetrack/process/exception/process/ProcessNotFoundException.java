package com.kevinj.portfolio.issuetrack.process.exception.process;

public class ProcessNotFoundException extends ProcessException {
    public ProcessNotFoundException() {
        super(ProcessErrorCode.PROCESS_NOT_FOUND);
    }
}
