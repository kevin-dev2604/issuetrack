package com.kevinj.portfolio.issuetrack.process.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProcessRepositiry extends JpaRepository<Process, Long> {
    Optional<Process> findByProcessIdAndUser(Long processId, Users user);
    Optional<Process> findByName(String name);
}
