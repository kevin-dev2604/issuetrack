package com.kevinj.portfolio.issuetrack.admin.exception;

public class InvalidTimezoneException extends AdminException {
    public InvalidTimezoneException() {
        super(AdminErrorCode.INVALID_TIMEZONE);
    }
}
