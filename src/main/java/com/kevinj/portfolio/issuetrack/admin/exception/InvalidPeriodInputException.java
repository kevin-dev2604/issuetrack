package com.kevinj.portfolio.issuetrack.admin.exception;

public class InvalidPeriodInputException extends AdminException {
    public InvalidPeriodInputException() {
        super(AdminErrorCode.INVALID_PERIOD_INPUT);
    }
}
