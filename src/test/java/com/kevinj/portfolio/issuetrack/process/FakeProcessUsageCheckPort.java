package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import com.kevinj.portfolio.issuetrack.process.application.port.ProcessUsageCheckPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;

public class FakeProcessUsageCheckPort implements ProcessUsageCheckPort {

    private final FakeIssuePort fakeIssuePort;

    public FakeProcessUsageCheckPort(FakeIssuePort fakeIssuePort) {
        this.fakeIssuePort = fakeIssuePort;
    }

    @Override
    public boolean isProcessUsing(User user, Long processId) {
        return fakeIssuePort.getAllIssueList()
                .stream()
                .anyMatch(issues -> issues.getProcessId().equals(processId));
    }
}
