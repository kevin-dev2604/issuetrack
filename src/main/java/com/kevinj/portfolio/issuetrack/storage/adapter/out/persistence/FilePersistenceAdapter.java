package com.kevinj.portfolio.issuetrack.storage.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.storage.application.port.out.FilePersistencePort;
import com.kevinj.portfolio.issuetrack.storage.domain.model.UploadFileDomain;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilePersistenceAdapter implements FilePersistencePort {

    private final JpaUploadFileRepository jpaUploadFileRepository;

    @Override
    public Long insertFile(String fileName, Long fileSize, String fileUri) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        if (fileName.startsWith(".")) {
            fileExtension = null;
        }

        UploadFiles uploadFiles = new UploadFiles(fileName, fileExtension, fileSize, fileUri);
        jpaUploadFileRepository.saveAndFlush(uploadFiles);

        return uploadFiles.getFileId();
    }

    @Override
    public String getFileUrl(Long fileId) {
        return jpaUploadFileRepository.findById(fileId)
            .map(UploadFiles::getFileUrl)
            .orElseThrow(() -> new EntityNotFoundException("Upload file row not found"));
    }

    @Override
    public void deleteFile(Long fileId) {
        jpaUploadFileRepository.deleteById(fileId);
    }

    @Override
    public List<UploadFileDomain> showFileInfoList(List<Long> fileIdList) {
        return jpaUploadFileRepository.findAllById(fileIdList)
            .stream()
            .map(f ->
                new UploadFileDomain(
                    f.getFileId(),
                    f.getFileName(),
                    f.getFileExtension(),
                    f.getFileSize(),
                    f.getFileUrl(),
                    f.getCreatedAt(),
                    f.getCreatedBy()
                )
            )
            .toList();
    }
}
