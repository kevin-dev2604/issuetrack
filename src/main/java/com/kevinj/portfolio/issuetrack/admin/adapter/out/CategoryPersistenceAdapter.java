package com.kevinj.portfolio.issuetrack.admin.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.JpaCategoryRepository;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.query.AdminQueryRepository;
import com.kevinj.portfolio.issuetrack.admin.application.dto.*;
import com.kevinj.portfolio.issuetrack.admin.application.port.CategoryManagePort;
import com.kevinj.portfolio.issuetrack.admin.domain.CategoryManageInfo;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryManagePort {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final AdminQueryRepository adminQueryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryManageInfoResponse> searchList(CategorySearchQuery query) {
        return adminQueryRepository.searchCategory(query);
    }

    @Override
    public Optional<CategoryManageInfo> getCategory(Long categoryId) {
        return jpaCategoryRepository.findById(categoryId)
                .map(categoryMapper::toCategoryMaangeDomain);
    }

    @Override
    public void addCategory(CategoryCreateCommand command) {

        Category parentCategory = getParentCategory(command.parentCategoryId());
        Integer depth = Objects.isNull(parentCategory) ? 1 : parentCategory.getDepth() + 1;

        Category category = new Category(null, parentCategory, command.label(), depth, YN.Y);

        jpaCategoryRepository.save(category);
    }

    @Override
    public void updateCategory(CategoryUpdateCommand command) {

        Category parentCategory = getParentCategory(command.parentCategoryId());
        Integer depth = Objects.isNull(parentCategory) ? 1 : parentCategory.getDepth() + 1;

        CategoryManageInfo categoryManageInfo = jpaCategoryRepository.findById(command.categoryId())
                .map(categoryMapper::toCategoryMaangeDomain)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        categoryManageInfo.update(command.parentCategoryId(), command.label(), depth, command.isUse());

        jpaCategoryRepository.save(categoryMapper.toCategoryEntity(categoryManageInfo, parentCategory));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = jpaCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        jpaCategoryRepository.delete(category);
    }

    @Override
    public boolean hasDuplicateCategory(Long categoryId, Long parentCategoryId, String label) {
        return adminQueryRepository.countSameLabelCategory(categoryId, parentCategoryId, label) > 0;
    }

    private Category getParentCategory(Long parentCategoryId) {
        if (parentCategoryId == null ||  parentCategoryId <= 0) {
            return null;
        }

        return jpaCategoryRepository.findById(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
    }
}
