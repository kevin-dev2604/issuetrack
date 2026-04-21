package com.kevinj.portfolio.issuetrack.dilemma.exception;

public class DiscussionNotFoundException extends DilemmaException {
    public DiscussionNotFoundException() {
        super(DilemmaErrorCode.DISCUSSION_NOT_FOUND);
    }
}
