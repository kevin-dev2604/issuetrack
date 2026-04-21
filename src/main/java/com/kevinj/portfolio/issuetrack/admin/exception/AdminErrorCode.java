package com.kevinj.portfolio.issuetrack.admin.exception;

import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;

public enum AdminErrorCode implements ErrorCode {
    // category
    NOT_FOUND_CATEGORY("Not found category"),
    WRONG_PARAMETERS_INPUT("Wrong parameters input"),
    CATEGORY_ALREADY_IN_USE("Category is already in use"),
    DUPLICATE_CATEGORY("Already exists this category"),

    // attributes
    NOT_FOUND_ATTRIBUTES("Not found attributes"),
    ATTRIBUTES_ALREADY_IN_USE("Attribute is already in use"),
    DUPLICATE_ATTRIBUTES("Already exists this attribute using the label"),

    // statistics
    NON_POSITIVE_DEPTH_VALUE("A non-positive depth value was entered."),
    INVALID_TIMEZONE("Invalid timezone value entered."),
    INVALID_PERIOD_INPUT("Invalid from or to value entered."),
    ;
    
    final String message;
    AdminErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return this.message;
    }
}
