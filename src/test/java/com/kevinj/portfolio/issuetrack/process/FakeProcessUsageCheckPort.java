package com.kevinj.portfolio.issuetrack.process;

import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;
import com.kevinj.portfolio.issuetrack.process.application.port.out.ProcessUsageCheckPort;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;

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
