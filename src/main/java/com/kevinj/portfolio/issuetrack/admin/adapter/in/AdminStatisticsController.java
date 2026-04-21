package com.kevinj.portfolio.issuetrack.admin.adapter.in;

import com.kevinj.portfolio.issuetrack.admin.application.IssueStatisticsUseCase;
import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    private final IssueStatisticsUseCase issueStatisticsUseCase;

    @Operation(
        summary = "Issue countings by status",
        description = "Show issue countings groupped by status"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/status")
    public List<IssueStatusCountRecordResponse> countByStatus() {
        return issueStatisticsUseCase.countByStatus();
    }

    @Operation(
        summary = "Issue countings by groupped category",
        description = "Show issue countings groupped by categroy depth"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/category/depth/{depth}")
    public List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(@PathVariable Integer depth) {
        return issueStatisticsUseCase.countByCategoryDepth(depth);
    }

    @Operation(
        summary = "Issue countings with category tree",
        description = "Show issue countings with categroy tree"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/category/tree")
    public List<IssueCategoryTreeCountRecordResponse> countByCategoryTree() {
        return issueStatisticsUseCase.countByCategoryTree();
    }

    @Operation(
        summary = "Issue creation statistics",
        description = "Show issue creation statistics between periods"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/creation")
    public IssueCreateDateCountResponse countCreationByPeriod(@RequestBody IssueCreateDateCountCommand createDateCountCommand) {
        return issueStatisticsUseCase.countCreationByPeriod(createDateCountCommand);
    }

    @Operation(
        summary = "Issue/Dilemma ratio statistics",
        description = "Show issue/dilemma ratio statistics groupped by top categories"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/category/dilemmaratio")
    public List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio() {
        return issueStatisticsUseCase.countCategoryIssueDilemmaRatio();
    }

}
