package com.kevinj.portfolio.issuetrack.auth.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLoginLogRepository extends JpaRepository<LoginLog, Long> {
}
