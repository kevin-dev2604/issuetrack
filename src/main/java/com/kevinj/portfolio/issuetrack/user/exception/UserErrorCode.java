package com.kevinj.portfolio.issuetrack.user.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    NO_INPUT("No imformation input."),
    NOT_FOUND_USER("Not found user."),
    NO_LOGIN_ID_INPUT("Login id is not found."),
    NO_PASSWORD_INPUT("Password is not found."),
    NO_NICKNAME_INPUT("Nickname is not found."),
    NO_EMAIL_INPUT("Email is not found."),
    WITHDRAWN_ID("Attempted access with a withdrawn ID"),
    DUPLICATE_LOGIN_ID("Duplicate login id found."),
    DUPLICATE_EMAIL("Duplicate email address."),
    WRONG_PASSWORD("Wrong password pattern."),
    WRONG_OLD_PASSWORD("No match old password."),
    WRONG_EMAIL("Wrong email address."),
    SAME_PASSWORD("New password is equal to old password."),
    ;

    private final String message;

    UserErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
