package com.kevinj.portfolio.issuetrack.exception;

public class TestConditionFailedException extends RuntimeException {
    public TestConditionFailedException() {
        super("Test condition error");
    }
}
