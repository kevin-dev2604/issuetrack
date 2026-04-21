package com.kevinj.portfolio.issuetrack.admin.exception;

public class DuplicateAttributesException extends AdminException {
    public DuplicateAttributesException() {
        super(AdminErrorCode.DUPLICATE_ATTRIBUTES);
    }
}
