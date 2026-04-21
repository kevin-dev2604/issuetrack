package com.kevinj.portfolio.issuetrack.admin.exception;

public class CategoryAlreadyInUseException extends AdminException {
    public CategoryAlreadyInUseException() {
        super(AdminErrorCode.CATEGORY_ALREADY_IN_USE);
    }
}
