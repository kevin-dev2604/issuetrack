package com.kevinj.portfolio.issuetrack.storage.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UploadFileDomain {
    private Long fileId;
    private String fileName;
    private String fileExtension;
    private Long fileSize;
    private String fileUrl;
    private LocalDateTime createdAt;
    private Long createdBy;
}
