package com.kevinj.portfolio.issuetrack.issue.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaIssueRepository extends JpaRepository<Issue, Long> {
    Optional<Issue> findByIssueIdAndUser(Long issueId, Users user);
}
