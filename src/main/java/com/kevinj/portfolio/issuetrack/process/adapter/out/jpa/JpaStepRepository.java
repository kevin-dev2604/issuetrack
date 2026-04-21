package com.kevinj.portfolio.issuetrack.process.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaStepRepository extends JpaRepository<Step, Long> {
    Optional<Step> findByStepIdAndProcess(Long stepId, Process process);
    List<Step> findAllByProcess(Process process);
}
