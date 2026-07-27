package com.kevinj.portfolio.issuetrack.issue.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.Attributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.Category;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence.Issue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence.IssueAttributes;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueAttributesDomain;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueDomain;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.persistence.Step;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
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
            issueDomain.getStatus(),
            null
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
