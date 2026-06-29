package com.kevinj.portfolio.issuetrack.admin.application.port.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.CategoryUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.domain.model.CategoryManageInfo;
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
