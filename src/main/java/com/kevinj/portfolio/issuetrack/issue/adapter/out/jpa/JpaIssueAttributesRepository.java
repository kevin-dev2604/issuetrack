package com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaIssueAttributesRepository extends JpaRepository<IssueAttributes, Long> {
    List<IssueAttributes> findByIssue(Issue issue);
}
