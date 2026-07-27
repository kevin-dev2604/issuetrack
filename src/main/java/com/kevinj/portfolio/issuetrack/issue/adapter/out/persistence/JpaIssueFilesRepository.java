package com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaIssueFilesRepository extends JpaRepository<IssueFiles, Long> {
    void deleteByIssue_IssueId(Long issueId);
}
