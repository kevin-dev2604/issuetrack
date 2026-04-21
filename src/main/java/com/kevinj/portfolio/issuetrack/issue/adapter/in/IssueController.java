package com.kevinj.portfolio.issuetrack.issue.adapter.in;

import com.kevinj.portfolio.issuetrack.global.dto.PageResponse;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import com.kevinj.portfolio.issuetrack.issue.application.IssueUseCase;
import com.kevinj.portfolio.issuetrack.issue.application.dto.*;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {

    private final IssueUseCase issueUseCase;

    @Operation(
        summary = "Create user issue",
        description = "Create user issue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "validation failed"),
        @ApiResponse(responseCode = "404", description = "not found category/process/step/etc"),
    })
    @PostMapping("/create")
    public void createIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody IssueCreateCommand command
    ) {
        issueUseCase.createIssue(userInfo.getUserId(), command);
    }

    @Operation(
        summary = "Update user issue",
        description = "Update user issue content"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "validation failed or disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/update")
    public void changeIssueInfo(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody IssueModifyCommand command
    ) {
        issueUseCase.changeIssueInfo(userInfo.getUserId(), command);
    }

    @Operation(
        summary = "Proceed user issue",
        description = "Move user issue to next step of process"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "last step or disabled status"),
        @ApiResponse(responseCode = "404", description = "not found process/issue"),
    })
    @PostMapping("/{issueId}/proceed")
    public void proceedIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.proceedIssue(userInfo.getUserId(), issueId);
    }

    @Operation(
        summary = "Pending user issue",
        description = "Change user issue status to 'pending'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/{issueId}/pending")
    public void pendingIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.changeStatus(userInfo.getUserId(), issueId, IssueStatus.PENDING);
    }

    @Operation(
        summary = "Hide user issue",
        description = "Change user issue status to 'hidden'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/{issueId}/hide")
    public void hideIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.changeStatus(userInfo.getUserId(), issueId, IssueStatus.HIDDEN);
    }

    @Operation(
        summary = "Restart user issue",
        description = "Change user issue status to 'handling'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/{issueId}/restart")
    public void restartIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.changeStatus(userInfo.getUserId(), issueId, IssueStatus.HANDLING);
    }

    @Operation(
        summary = "Exit user issue",
        description = "Change user issue status to 'exit'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/{issueId}/terminate")
    public void terminateIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.changeStatus(userInfo.getUserId(), issueId, IssueStatus.EXIT);
    }

    @Operation(
        summary = "Delete user issue",
        description = "Delete user issue. Cannot search it after this action"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @DeleteMapping("/{issueId}")
    public void deleteIssue(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        issueUseCase.deleteIssue(userInfo.getUserId(), issueId);
    }

    @Operation(
        summary = "Change process of user issue",
        description = "Change process of user issue. Current step is initialized."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/{issueId}/process/{processId}")
    public void changeIssueProcess(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId,
            @PathVariable Long processId
    ) {
        issueUseCase.changeProcess(userInfo.getUserId(), issueId, processId);
    }

    @Operation(
        summary = "Search user issue",
        description = "Search user issue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/search")
    public PageResponse<IssueSearchResponse> searchIssues(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody IssueSearchCommand searchCommand
    ) {
        return PageResponse.from(issueUseCase.searchIssues(userInfo.getUserId(), searchCommand.toQuery()));
    }

    @Operation(
        summary = "Detailed user issue",
        description = "Return detailed user issue information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/{issueId}")
    public IssueDetailResponse getIssueDetail(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long issueId
    ) {
        return issueUseCase.getIssueDetails(userInfo.getUserId(), issueId);
    }

}
