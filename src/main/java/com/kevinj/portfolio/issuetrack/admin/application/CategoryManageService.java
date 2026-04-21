package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.port.AdminUsageCheckPort;
import com.kevinj.portfolio.issuetrack.admin.application.port.CategoryManagePort;
import com.kevinj.portfolio.issuetrack.admin.exception.CategoryAlreadyInUseException;
import com.kevinj.portfolio.issuetrack.admin.exception.DuplicateCategoryException;
import com.kevinj.portfolio.issuetrack.admin.exception.NotFoundCategoryException;
import com.kevinj.portfolio.issuetrack.admin.exception.WrongParametersInputException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryManageService implements CategoryManageUseCase{

    private final CategoryManagePort categoryManagePort;
    private final AdminUsageCheckPort adminUsageCheckPort;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryManageInfoResponse> searchCategoryManageList(CategorySearchQuery query) {
        return categoryManagePort.searchList(query);
    }

    @Override
    public CategoryManageInfoResponse getCategoryManageInfo(Long categoryId) {
        return categoryManagePort.getCategory(categoryId)
                .map(categoryMapper::toCategoryManageInfoResponse)
                .orElseThrow(NotFoundCategoryException::new);
    }

    @Override
    public void createCategory(CategoryCreateCommand command) {
        if (!validateCategoryInfo(command.parentCategoryId(), command.label(), command.isUse())) {
            throw new WrongParametersInputException();
        } else if (categoryManagePort.hasDuplicateCategory(null, command.parentCategoryId(), command.label())) {
            throw new DuplicateCategoryException();
        }

        categoryManagePort.addCategory(command);
    }

    @Override
    public void updateCategory(CategoryUpdateCommand command) {
        if (!validateCategoryInfo(command.parentCategoryId(), command.label(), command.isUse())) {
            throw new WrongParametersInputException();
        } else if (categoryManagePort.getCategory(command.categoryId()).isEmpty()) {
            throw new NotFoundCategoryException();
        } else if (categoryManagePort.hasDuplicateCategory(command.categoryId(), command.parentCategoryId(), command.label())) {
            throw new DuplicateCategoryException();
        } else if (adminUsageCheckPort.isCategoryUsing(command.categoryId())) {
            throw new CategoryAlreadyInUseException();
        }

        categoryManagePort.updateCategory(command);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (categoryManagePort.getCategory(categoryId).isEmpty()) {
            throw new NotFoundCategoryException();
        } else if (adminUsageCheckPort.isCategoryUsing(categoryId)) {
            throw new CategoryAlreadyInUseException();
        }

        categoryManagePort.deleteCategory(categoryId);
    }

    private boolean validateCategoryInfo(Long parentCategoryId, String label, YN isUse) {
        if ((parentCategoryId != null && parentCategoryId <= 0)
                || label == null || label.isBlank()
                || isUse == null
        ) {
            return false;
        }

        return true;
    }
}
