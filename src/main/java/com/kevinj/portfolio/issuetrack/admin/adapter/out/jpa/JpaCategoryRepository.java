package com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByLabel(String label);
}
