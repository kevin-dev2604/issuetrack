package com.kevinj.portfolio.issuetrack.storage.application.port.out;

import com.kevinj.portfolio.issuetrack.storage.domain.model.UploadFileDomain;

import java.util.List;

public interface FilePersistencePort {
    Long insertFile(String fileName, Long fileSize, String fileUri);
    String getFileUrl(Long fileId);
    void deleteFile(Long fileId);
    List<UploadFileDomain> showFileInfoList(List<Long> fileIdList);
}
