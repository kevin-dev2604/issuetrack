package com.kevinj.portfolio.issuetrack.admin.exception;

public class NotFoundAttributesException extends AdminException {
    public NotFoundAttributesException() {
        super(AdminErrorCode.NOT_FOUND_ATTRIBUTES);
    }
}
