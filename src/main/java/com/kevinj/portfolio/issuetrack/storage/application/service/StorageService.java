package com.kevinj.portfolio.issuetrack.storage.application.service;

import com.kevinj.portfolio.issuetrack.storage.application.port.in.StorageUseCase;
import com.kevinj.portfolio.issuetrack.storage.application.port.out.FilePersistencePort;
import com.kevinj.portfolio.issuetrack.storage.application.port.out.StoragePort;
import com.kevinj.portfolio.issuetrack.storage.exception.EmptyFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class StorageService implements StorageUseCase {

    private final StoragePort storagePort;
    private final FilePersistencePort filePersistencePort;

    @Override
    public Long upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException();
        }

        try {
            Long fileSize = file.getSize();
            String fileUrl = storagePort.upload(file);

            return filePersistencePort.insertFile(file.getOriginalFilename(),  fileSize, fileUrl);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public void delete(Long fileId) {
        if (fileId == null) {
            throw new EmptyFileException();
        }


        try {
            String fileUrl = filePersistencePort.getFileUrl(fileId);
            storagePort.delete(fileUrl);

            filePersistencePort.deleteFile(fileId);

        } catch (IOException e) {
            throw new RuntimeException("File deletion failed", e);
        }
    }
}
