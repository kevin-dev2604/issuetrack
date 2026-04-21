package com.kevinj.portfolio.issuetrack.admin.exception;

public class NotFoundCategoryException extends AdminException {
    public NotFoundCategoryException() {
        super(AdminErrorCode.NOT_FOUND_CATEGORY);
    }
}
