package com.kevinj.portfolio.issuetrack.storage.application.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface StorageUseCase {
    Long upload(MultipartFile file);
    void delete(Long fileId);
}
