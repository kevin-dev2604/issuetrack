package com.kevinj.portfolio.issuetrack.process.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProcessRepository extends JpaRepository<Process, Long> {
    Optional<Process> findByProcessIdAndUser(Long processId, Users user);
    Optional<Process> findByName(String name);
}
