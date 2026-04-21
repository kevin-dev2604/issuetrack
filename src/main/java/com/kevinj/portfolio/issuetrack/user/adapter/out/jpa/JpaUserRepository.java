package com.kevinj.portfolio.issuetrack.user.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLoginId(String LoginId);
    Optional<Users> findByEmail(String email);
}
