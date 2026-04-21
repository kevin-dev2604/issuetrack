package com.kevinj.portfolio.issuetrack.admin.application.port;

import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.domain.CategoryManageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryManagePort {
    Page<CategoryManageInfoResponse> searchList(CategorySearchQuery query);
    Optional<CategoryManageInfo> getCategory(Long categoryId);
    void addCategory(CategoryCreateCommand command);
    void updateCategory(CategoryUpdateCommand command);
    void deleteCategory(Long categoryId);
    boolean hasDuplicateCategory(Long categoryId, Long parentCategoryId, String label);
}
