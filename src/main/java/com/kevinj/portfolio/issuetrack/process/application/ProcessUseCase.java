package com.kevinj.portfolio.issuetrack.process.application;

import com.kevinj.portfolio.issuetrack.process.application.dto.process.*;
import org.springframework.data.domain.Page;

public interface ProcessUseCase {

    Page<ProcessInfoResponse> searchProcesses(ProcessSearchQuery query);
    ProcessInfoResponse getProcessInfo(Long userId, Long processId);
    void createProcessInfo(Long userId, ProcessCreateCommand createCommand);
    void updateProcessInfo(Long userId, ProcessUpdateCommand updateCommand);
    ProcessDeleteResponse deleteProcessInfo(Long userId, Long processId);
}
