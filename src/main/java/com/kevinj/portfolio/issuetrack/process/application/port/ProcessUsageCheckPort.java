package com.kevinj.portfolio.issuetrack.process.application.port;

import com.kevinj.portfolio.issuetrack.user.domain.User;

public interface ProcessUsageCheckPort {
    boolean isProcessUsing(User user, Long processId);
}
