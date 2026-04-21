package com.kevinj.portfolio.issuetrack.dilemma.adapter.in;

import com.kevinj.portfolio.issuetrack.dilemma.application.DilemmaUseCase;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.*;
import com.kevinj.portfolio.issuetrack.global.dto.PageResponse;
import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dilemma")
public class DilemmaController {

    private final DilemmaUseCase dilemmaUseCase;

    @Operation(
        summary = "Open dilemma",
        description = "Create dilemma from user issue"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "disabled status"),
        @ApiResponse(responseCode = "404", description = "not found issue"),
    })
    @PostMapping("/user/open")
    public void openDilemma(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaCreateCommand dilemmaCreateCommand
    ) {
        dilemmaUseCase.openDilemma(userInfo.getUserId(),  dilemmaCreateCommand);
    }

    @Operation(
        summary = "Edit dilemma",
        description = "Edit dilemma content"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "already closed dilemma"),
        @ApiResponse(responseCode = "404", description = "not found dilemma"),
    })
    @PostMapping("/user/edit")
    public void editDilemma(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaEditCommand dilemmaEditCommand
    ) {
        dilemmaUseCase.editDilemma(userInfo.getUserId(), dilemmaEditCommand);
    }

    @Operation(
        summary = "Search user dilemma",
        description = "User can dilemma created by the user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/user/search")
    public PageResponse<DilemmaSearchResponse> searchMyDilemma(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaUserSearchCommand userSearchCommand
    ) {
        return PageResponse.from(dilemmaUseCase.searchMyDilemmaList(userInfo.getUserId(), userSearchCommand.toQuery()));
    }

    @Operation(
        summary = "Create discussion",
        description = "Create dilemma discussion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found dilemma"),
    })
    @PostMapping("/discussion/create")
    public void createDilemmaDiscussion(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaDiscussionCreateCommand dilemmaDiscussionCreateCommand
    ) {
        dilemmaUseCase.createDiscussion(userInfo.getUserId(), dilemmaDiscussionCreateCommand);
    }

    @Operation(
        summary = "Edit discussion",
        description = "Edit dilemma discussion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found dilemma discussion"),
    })
    @PostMapping("/discussion/edit")
    public void editDilemmaDiscussion(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaDiscussionEditCommand dilemmaDiscussionEditCommand
    ) {
        dilemmaUseCase.editDiscussion(userInfo.getUserId(), dilemmaDiscussionEditCommand);
    }

    @Operation(
        summary = "Delete discussion",
        description = "Delete dilemma discussion"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found dilemma"),
    })
    @DeleteMapping("/discussion")
    public void deleteDilemmaDiscussion(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody DilemmaDiscussionDeleteCommand dilemmaDiscussionDeleteCommand
    ) {
        dilemmaUseCase.deleteDiscussion(userInfo.getUserId(), dilemmaDiscussionDeleteCommand);
    }

    @Operation(
        summary = "Close dilemma",
        description = "User roled 'DILEMMA' can close the dilemma"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found dilemma/issue"),
    })
    @PostMapping("/{dilemmaId}/close")
    public void closeDilemma(
            @PathVariable Long dilemmaId
    ) {
        dilemmaUseCase.closeDilemma(dilemmaId);
    }

    @Operation(
        summary = "Search dilemma",
        description = "Search all dilemma without any role"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/search")
    public PageResponse<DilemmaSearchResponse> searchFullDilemma(
        @RequestBody DilemmaSearchCommand dilemmaSearchCommand
    ) {
        return PageResponse.from(dilemmaUseCase.searchFullDilemmaList(dilemmaSearchCommand.toQuery()));
    }

    @Operation(
        summary = "Get dilemma details",
        description = "Return information about the dilemma"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/{dilemmaId}")
    public DilemmaDetailResponse getDilemmaDetails(
            @PathVariable Long dilemmaId
    ) {
        return dilemmaUseCase.getDilemmaDetailInfo(dilemmaId);
    }

}
