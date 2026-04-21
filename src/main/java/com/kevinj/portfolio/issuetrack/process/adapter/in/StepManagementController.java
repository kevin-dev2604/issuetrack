package com.kevinj.portfolio.issuetrack.process.adapter.in;

import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import com.kevinj.portfolio.issuetrack.process.application.StepUseCase;
import com.kevinj.portfolio.issuetrack.process.application.dto.step.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StepManagementController {

    private final StepUseCase stepUseCase;

    @Operation(
        summary = "Get all step management list",
        description = "Return all step management list of a process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/process/{processId}/step/all")
    public List<StepInfoResponse> getAllStepList(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long processId

    ) {
        return stepUseCase.getAllStepList(userInfo.getUserId(), processId);
    }

    @Operation(
        summary = "Get active step list",
        description = "Return active step list of a process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/process/{processId}/step/active")
    public List<StepInfoResponse> getActiveStepList(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long processId
    ) {
        return stepUseCase.getActiveStepList(userInfo.getUserId(), processId);
    }

    @Operation(
        summary = "Get step details",
        description = "Return one step details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found step info"),
    })
    @GetMapping("/process/step")
    public StepInfoResponse getStepInfo(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody StepCommand stepCommand
    ) {
        return stepUseCase.getStepInfo(userInfo.getUserId(), stepCommand);
    }

    @Operation(
        summary = "Create one step",
        description = "Create one step of a process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found process info"),
    })
    @PostMapping("/process/step")
    public void createStep(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody StepCreateCommand createCommand
    ) {
        stepUseCase.createStepInfo(userInfo.getUserId(), createCommand);
    }

    @Operation(
        summary = "Create step list",
        description = "Create step list of a process one time"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found process info"),
    })
    @PostMapping("/process/step/list")
    public void createStepList(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody StepListCreateCommand listCreateCommand
            ) {
        stepUseCase.createStepList(userInfo.getUserId(), listCreateCommand);
    }

    @Operation(
        summary = "Update one step",
        description = "Update one step of a process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found process/step"),
    })
    @PostMapping("/process/step/update")
    public void updateStep(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody StepUpdateCommand updateCommand
    ) {
        stepUseCase.updateStepInfo(userInfo.getUserId(), updateCommand);
    }

    @Operation(
        summary = "Delete one step",
        description = "Delete one step of a process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found process/step"),
    })
    @DeleteMapping("/process/step")
    public void deleteStep(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody StepCommand command
    ) {
        stepUseCase.deleteStepInfo(userInfo.getUserId(), command);
    }

}
