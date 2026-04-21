package com.kevinj.portfolio.issuetrack.process.adapter.in;

import com.kevinj.portfolio.issuetrack.global.dto.PageResponse;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import com.kevinj.portfolio.issuetrack.process.application.ProcessUseCase;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessCreateCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessSearchCommand;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessUpdateCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
public class ProcessManagementController {

    private final ProcessUseCase processUseCase;

    @Operation(
        summary = "Search user process",
        description = "Users can search their process list"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/search")
    public PageResponse<ProcessInfoResponse> searchProcesses(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            ProcessSearchCommand command
    ) {
        return PageResponse.from(processUseCase.searchProcesses(command.toQuery(userInfo.getUserId())));
    }

    @Operation(
        summary = "Process detail",
        description = "Users can read their process details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found process"),
    })
    @GetMapping("/{processId}")
    public ProcessInfoResponse getProcessInfo(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long processId
    ) {
        return processUseCase.getProcessInfo(userInfo.getUserId(), processId);
    }

    @Operation(
        summary = "Create process",
        description = "Users can create their process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
    })
    @PostMapping("/create")
    public void createProcess(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody ProcessCreateCommand command
    ) {
        processUseCase.createProcessInfo(userInfo.getUserId(), command);
    }

    @Operation(
        summary = "Update process",
        description = "Users can update their process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found process"),
    })
    @PostMapping("/update")
    public void updateProcess(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody ProcessUpdateCommand command
    ) {
        processUseCase.updateProcessInfo(userInfo.getUserId(), command);
    }

    @Operation(
        summary = "Delete process",
        description = "Users can delete their process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found process"),
        @ApiResponse(responseCode = "409", description = "used process cannot be deleted"),
    })
    @DeleteMapping("/delete/{processId}")
    public void deleteProcess(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long processId
    ) {
        processUseCase.deleteProcessInfo(userInfo.getUserId(), processId);
    }
}
