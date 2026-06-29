package com.kevinj.portfolio.issuetrack.process.application.port.out;

import com.kevinj.portfolio.issuetrack.user.domain.model.User;

public interface ProcessUsageCheckPort {
    boolean isProcessUsing(User user, Long processId);
}
