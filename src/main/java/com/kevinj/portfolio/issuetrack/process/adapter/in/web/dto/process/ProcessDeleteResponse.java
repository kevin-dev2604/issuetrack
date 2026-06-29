package com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.process;

import com.kevinj.portfolio.issuetrack.process.domain.model.enums.DeleteMessageStatus;

public record ProcessDeleteResponse(
        DeleteMessageStatus status,
        String message
) {
    public static ProcessDeleteResponse deleted() {
        return new ProcessDeleteResponse(DeleteMessageStatus.DELETED, "Process successfully deleted.");
    }
    public static ProcessDeleteResponse deActivated() {
        return new ProcessDeleteResponse(DeleteMessageStatus.DEACTIVATED, "Process deactivated instead of deleted as it is in use by issues.");
    }
}
