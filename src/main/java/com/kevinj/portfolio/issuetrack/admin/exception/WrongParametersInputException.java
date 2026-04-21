package com.kevinj.portfolio.issuetrack.admin.exception;

public class WrongParametersInputException extends AdminException {
    public WrongParametersInputException() {
        super(AdminErrorCode.WRONG_PARAMETERS_INPUT);
    }
}
