package com.kevinj.portfolio.issuetrack.admin.application.port.in;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryUpdateCommand;
import org.springframework.data.domain.Page;

public interface CategoryManageUseCase {
    Page<CategoryManageInfoResponse> searchCategoryManageList(CategorySearchQuery query);
    CategoryManageInfoResponse getCategoryManageInfo(Long categoryId);
    void createCategory(CategoryCreateCommand command);
    void updateCategory(CategoryUpdateCommand command);
    void deleteCategory(Long categoryId);
}
