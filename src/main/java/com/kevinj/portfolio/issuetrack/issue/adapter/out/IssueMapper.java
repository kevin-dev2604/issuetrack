package com.kevinj.portfolio.issuetrack.issue.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Attributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.Issue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.IssueAttributes;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueDomain;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueMapper {

    public Issue toIssueEntity(
            Long issueId,
            Users user,
            Category category,
            List<IssueAttributes> issueAttributesList,
            Process process,
            Step currentStep,
            IssueDomain issueDomain
    ) {
        return new Issue(
            issueId,
            user,
            category,
            issueAttributesList,
            process,
            currentStep,
            issueDomain.getTitle(),
            issueDomain.getDetails(),
            issueDomain.getStatus()
        );

    }

    public IssueDomain toIssueDomain(
            Issue issue
    ) {
        return new IssueDomain(
                issue.getIssueId(),
                issue.getUser().getUserId(),
                issue.getCategory().getCategoryId(),
                issue.getIssueAttributesList().stream().map(this::toIssueAttributesDomain).toList(),
                issue.getProcess().getProcessId(),
                issue.getCurrentStep().getStepId(),
                issue.getTitle(),
                issue.getDetails(),
                issue.getStatus(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }

    public IssueAttributes toIssueAttributesEntity(Long id, Issue issue, Attributes attributes, String value) {
        return new IssueAttributes(
                id,
                issue,
                attributes,
                value
        );
    }

    public IssueAttributesDomain toIssueAttributesDomain(IssueAttributes issueAttributes) {
        return new IssueAttributesDomain(
                issueAttributes.getId(),
                issueAttributes.getIssue().getIssueId(),
                issueAttributes.getAttributes().getAttributesId(),
                issueAttributes.getValue(),
                issueAttributes.getCreatedAt(),
                issueAttributes.getUpdatedAt()
        );
    }
}
