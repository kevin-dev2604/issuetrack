package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryUpdateCommand;
import org.springframework.data.domain.Page;

public interface CategoryManageUseCase {
    Page<CategoryManageInfoResponse> searchCategoryManageList(CategorySearchQuery query);
    CategoryManageInfoResponse getCategoryManageInfo(Long categoryId);
    void createCategory(CategoryCreateCommand command);
    void updateCategory(CategoryUpdateCommand command);
    void deleteCategory(Long categoryId);
}
