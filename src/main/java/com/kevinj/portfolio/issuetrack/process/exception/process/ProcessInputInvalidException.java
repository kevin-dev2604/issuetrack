package com.kevinj.portfolio.issuetrack.process.exception.process;

public class ProcessInputInvalidException extends ProcessException {
    public ProcessInputInvalidException() {
        super(ProcessErrorCode.PROCESS_INPUT_INVALID);
    }
}
