package com.kevinj.portfolio.issuetrack.storage.adapter.out.storage;

import com.kevinj.portfolio.issuetrack.storage.application.port.out.StoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Profile({"local", "dev"})
public class LocalStorageAdapter implements StoragePort {

    @Value("${storage.local.upload-dir}")
    private String uploadDir;

    @Value("${storage.local.base-url}")
    private String baseUrl;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String storeFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = Paths.get(uploadDir).resolve(storeFileName).normalize();

        Files.createDirectories(targetPath.getParent());
        file.transferTo(targetPath.toFile());
        return baseUrl + storeFileName;
    }

    @Override
    public void delete(String fileUrl) throws IOException {
        String fileName = fileUrl.replace(baseUrl, "");
        Path targetPath = Paths.get(uploadDir).resolve(fileName).normalize();
        Files.deleteIfExists(targetPath);
    }
}
