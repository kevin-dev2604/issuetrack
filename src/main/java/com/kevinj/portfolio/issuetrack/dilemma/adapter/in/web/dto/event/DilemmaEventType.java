package com.kevinj.portfolio.issuetrack.dilemma.adapter.in.web.dto.event;

import lombok.Getter;

@Getter
public enum DilemmaEventType {
    DILEMMA_CREATED("dilemma-created"),
    DILEMMA_EDITED("dilemma-edited"),
    DILEMMA_CLOSED("dilemma-closed"),
    DISCUSSION_CREATED("discussion-created"),
    DISCUSSION_EDITED("discussion-edited"),
    ;

    private final String value;

    DilemmaEventType(String value) {
        this.value = value;
    }

}
