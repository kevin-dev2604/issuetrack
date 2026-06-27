package com.kevinj.portfolio.issuetrack.user.exception;

public class FcmTokenInvalidException extends UserException {
    public FcmTokenInvalidException() {
        super(UserErrorCode.FCM_TOKEN_INVALID);
    }
}
