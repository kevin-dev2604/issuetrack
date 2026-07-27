package com.kevinj.portfolio.issuetrack.storage.application.port.out;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoragePort {
    String upload(MultipartFile file) throws IOException;
    void delete(String fileUrl) throws IOException;
}
