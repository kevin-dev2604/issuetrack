package com.kevinj.portfolio.issuetrack.admin;

import com.kevinj.portfolio.issuetrack.admin.application.port.AdminUsageCheckPort;
import com.kevinj.portfolio.issuetrack.issue.FakeIssuePort;

public class FakeAdminUsageCheckPort implements AdminUsageCheckPort {

    private final FakeIssuePort fakeIssuePort;

    public FakeAdminUsageCheckPort(FakeIssuePort fakeIssuePort) {
        this.fakeIssuePort = fakeIssuePort;
    }

    @Override
    public boolean isCategoryUsing(Long categoryId) {
        return fakeIssuePort.getAllIssueList()
                .stream()
                .anyMatch(issue -> issue.getCategoryId().equals(categoryId));
    }

    @Override
    public boolean isAttributesUsing(Long attributeId) {
        return fakeIssuePort.getAllIssueAttributesList()
                .stream()
                .anyMatch(issueAttributes -> issueAttributes.getAttributesId().equals(attributeId));
    }
}
