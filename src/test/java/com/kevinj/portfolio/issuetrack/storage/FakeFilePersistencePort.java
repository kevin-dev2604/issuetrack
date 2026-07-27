package com.kevinj.portfolio.issuetrack.storage;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.issue.domain.model.IssueDomain;
import com.kevinj.portfolio.issuetrack.storage.application.port.out.FilePersistencePort;
import com.kevinj.portfolio.issuetrack.storage.domain.model.UploadFileDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeFilePersistencePort implements FilePersistencePort, FakePort {

    private final Map<Long, UploadFileDomain> fileDomainMap = new HashMap<>();

    @Override
    public Long insertFile(String fileName, Long fileSize, String fileUrl) {
        Long fileId = newId();
        UploadFileDomain domain = new UploadFileDomain(fileId, fileName, null, fileSize, fileUrl, null, null);

        fileDomainMap.put(fileId, domain);

        return fileId;
    }

    @Override
    public String getFileUrl(Long fileId) {
        return fileDomainMap.get(fileId).getFileUrl();
    }

    @Override
    public void deleteFile(Long fileId) {
        fileDomainMap.remove(fileId);
    }

    @Override
    public List<UploadFileDomain> showFileInfoList(List<Long> fileIdList) {
        List<UploadFileDomain> result = new ArrayList<>();

        for (Long fileId : fileIdList) {
            if(fileDomainMap.containsKey(fileId)) {
                result.add(fileDomainMap.get(fileId));
            }
        }
        return result;
    }

    @Override
    public Long newId() {
        return (long) fileDomainMap.size() + 1;
    }

    @Override
    public Long lastId() {
        return fileDomainMap.keySet()
            .stream()
            .reduce(Long::max)
            .orElse(null);
    }
}
