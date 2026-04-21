package com.kevinj.portfolio.issuetrack.admin.exception;

public class NonPositiveDepthValueException extends AdminException {
    public NonPositiveDepthValueException() {
        super(AdminErrorCode.NON_POSITIVE_DEPTH_VALUE);
    }
}
