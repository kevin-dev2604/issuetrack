package com.kevinj.portfolio.issuetrack.process.application.port;

import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessSearchQuery;
import com.kevinj.portfolio.issuetrack.process.domain.ProcessDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProcessPort {

    Page<ProcessInfoResponse> searchProcesses(User user, ProcessSearchQuery query);

    // Search by User/Process ID (including Deletion Check)
    Optional<ProcessDomain> getProcess(User user, Long processId);
    // Search by User/Process ID (excluding Deletion Check)
    Optional<ProcessDomain> getProcessIncludingDeleted(User user, Long processId);
    // Search by Process ID only, excluding user and deletion status
    Optional<ProcessDomain> getProcessUnscoped(Long processId);

    void createProcess(User user, ProcessCreateCommand command);
    void saveProcess(ProcessDomain process, User user);

    boolean isUserProcess(User user, Long processId);

}
