package com.kevinj.portfolio.issuetrack.admin.exception;

public class DuplicateCategoryException extends AdminException {
    public DuplicateCategoryException() {
        super(AdminErrorCode.DUPLICATE_CATEGORY);
    }
}
