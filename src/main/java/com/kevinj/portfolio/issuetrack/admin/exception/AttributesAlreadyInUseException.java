package com.kevinj.portfolio.issuetrack.admin.exception;

public class AttributesAlreadyInUseException extends AdminException {
    public AttributesAlreadyInUseException() {
        super(AdminErrorCode.ATTRIBUTES_ALREADY_IN_USE);
    }
}
