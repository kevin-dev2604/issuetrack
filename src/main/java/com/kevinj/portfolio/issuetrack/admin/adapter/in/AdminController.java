package com.kevinj.portfolio.issuetrack.admin.adapter.in;

import com.kevinj.portfolio.issuetrack.admin.application.AttributesManageService;
import com.kevinj.portfolio.issuetrack.admin.application.CategoryManageService;
import com.kevinj.portfolio.issuetrack.admin.application.dto.*;
import com.kevinj.portfolio.issuetrack.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final CategoryManageService categoryManageService;
    private final AttributesManageService attributesManageService;

    @Operation(
        summary = "Category Search",
        description = "Admin account can search category information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/admin/category/search")
    public PageResponse<CategoryManageInfoResponse> searchCategoryManageList(@RequestBody CategorySearchCommand command) {
        return PageResponse.from(categoryManageService.searchCategoryManageList(command.toQuery()));
    }

    @Operation(
        summary = "Category details",
        description = "Admin account can read category detailed information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found category"),
    })
    @GetMapping("/admin/category/{categoryId}")
    public CategoryManageInfoResponse getCategoryManageInfo(@PathVariable("categoryId") Long categoryId) {
        return categoryManageService.getCategoryManageInfo(categoryId);
    }

    @Operation(
        summary = "Create category",
        description = "Admin account can create category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
    })
    @PostMapping("/admin/category/create")
    public void createCategoryManageInfo(@RequestBody CategoryCreateCommand createCommand) {
        categoryManageService.createCategory(createCommand);
    }

    @Operation(
        summary = "Update category",
        description = "Admin account can update category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found category"),
    })
    @PostMapping("/admin/category/update")
    public void updateCategoryManageInfo(@RequestBody CategoryUpdateCommand updateCommand) {
        categoryManageService.updateCategory(updateCommand);
    }

    @Operation(
        summary = "Delete category",
        description = "Admin account can delete category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found category"),
    })
    @DeleteMapping("/admin/category/{categoryId}")
    public void deleteCategoryManageInfo(@PathVariable("categoryId") Long categoryId) {
        categoryManageService.deleteCategory(categoryId);
    }

    @Operation(
        summary = "Attributes Search",
        description = "Admin account can search attributes information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/admin/attributes/search")
    public PageResponse<AttributesManageInfoResponse> searchAttributesManageList(@RequestBody AttributesSearchCommand command) {
        return PageResponse.from(attributesManageService.searchAttributeManageInfo(command.toQuery()));
    }

    @Operation(
        summary = "Attributes details",
        description = "Admin account can read attributes information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found attributes"),
    })
    @GetMapping("/admin/attributes/{attributesId}")
    public AttributesManageInfoResponse getAttributesManageInfo(@PathVariable("attributesId") Long attributesId) {
        return attributesManageService.getAttributesManageInfo(attributesId);
    }

    @Operation(
        summary = "Create attributes",
        description = "Admin account can create attributes infomation"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
    })
    @PostMapping("/admin/attributes/create")
    public void createAttributesManageInfo(@RequestBody AttributesCreateCommand createCommand) {
        attributesManageService.createAttributeManage(createCommand);
    }

    @Operation(
        summary = "Update attributes",
        description = "Admin account can update attributes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation failed"),
        @ApiResponse(responseCode = "404", description = "not found attributes"),
    })
    @PostMapping("/admin/attributes/update")
    public void updateAttributesManageInfo(@RequestBody AttributesUpdateCommand updateCommand) {
        attributesManageService.updateAttributeManage(updateCommand);
    }

    @Operation(
        summary = "Delete attributes",
        description = "Admin account can delete attributes"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found attributes"),
    })
    @DeleteMapping("/admin/attributes/{attributesId}")
    public void deleteAttributesManageInfo(@PathVariable("attributesId") Long attributesId) {
        attributesManageService.deleteAttributeManage(attributesId);
    }
}
