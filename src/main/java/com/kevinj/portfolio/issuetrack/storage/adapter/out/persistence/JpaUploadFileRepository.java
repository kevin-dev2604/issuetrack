package com.kevinj.portfolio.issuetrack.storage.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUploadFileRepository extends JpaRepository<UploadFiles, Long> {
}
