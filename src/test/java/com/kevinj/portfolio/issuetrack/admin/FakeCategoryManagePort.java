package com.kevinj.portfolio.issuetrack.admin;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.*;
import com.kevinj.portfolio.issuetrack.admin.application.port.CategoryManagePort;
import com.kevinj.portfolio.issuetrack.admin.domain.CategoryManageInfo;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import com.kevinj.portfolio.issuetrack.global.time.TimeProvider;
import org.springframework.data.domain.*;

import java.util.*;

public class FakeCategoryManagePort implements CategoryManagePort, FakePort {

    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final Map<Long, CategoryManageInfo> categoryManageInfoList = new HashMap<>();
    private final CategoryMapper categoryMapper;

    public FakeCategoryManagePort(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Page<CategoryManageInfoResponse> searchList(CategorySearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("depth"), Sort.Order.asc("label"), Sort.Order.desc("createdAt"))
        );

        List<CategoryManageInfoResponse> content = categoryManageInfoList.values()
                .stream()
                .filter(category -> {
                    Boolean result = true;
                    if (query.label() != null && !query.label().isBlank()) {
                        result &= category.getLabel().contains(query.label());
                    }
                    if (query.depth() != null && query.depth() > 0) {
                        result &= category.getDepth() == query.depth();
                    }
                    if (query.isUse() != null) {
                        result &= category.getIsUse().equals(query.isUse());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(categoryMapper::toCategoryManageInfoResponse)
                .toList();

        Long total = categoryManageInfoList.values()
                .stream()
                .filter(category -> {
                    Boolean result = true;
                    if (query.label() != null && !query.label().isBlank()) {
                        result &= category.getLabel().contains(query.label());
                    }
                    if (query.depth() != null && query.depth() > 0) {
                        result &= category.getDepth() == query.depth();
                    }
                    if (query.isUse() != null) {
                        result &= category.getIsUse().equals(query.isUse());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<CategoryManageInfo> getCategory(Long categoryId) {
        return Optional.ofNullable(categoryManageInfoList.get(categoryId));
    }

    @Override
    public void addCategory(CategoryCreateCommand command) {
        Long categoryId = (long) (categoryManageInfoList.size() + 1);
        CategoryManageInfo parentCategory = categoryManageInfoList.get(command.parentCategoryId());
        Integer depth = Objects.isNull(parentCategory) ? 1 : parentCategory.getDepth() + 1;
        CategoryManageInfo category = new CategoryManageInfo(
                categoryId,
                command.parentCategoryId(),
                command.label(),
                depth,
                command.isUse(),
                null,
                timeProvider.now(),
                timeProvider.now()
        );

        categoryManageInfoList.put(categoryId, category);
    }

    @Override
    public void updateCategory(CategoryUpdateCommand command) {
        CategoryManageInfo category = categoryManageInfoList.get(command.categoryId());
        CategoryManageInfo parentCategory = categoryManageInfoList.get(command.parentCategoryId());
        Integer depth = Objects.isNull(parentCategory) ? 1 : parentCategory.getDepth() + 1;

        category.update(command.parentCategoryId(), command.label(), depth, command.isUse());

    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryManageInfoList.remove(categoryId);
    }

    @Override
    public boolean hasDuplicateCategory(Long categoryId, Long parentCategoryId, String label) {
        return categoryManageInfoList.values()
                .stream()
                .anyMatch(category ->
                        !Objects.equals(category.getCategoryId(), categoryId) &&
                        Objects.equals(category.getParentCategoryId(), parentCategoryId) &&
                        category.getLabel().equals(label)
                );
    }

    @Override
    public Long newId() {
        return (long) categoryManageInfoList.size() + 1;
    }

    @Override
    public Long lastId() {
        return categoryManageInfoList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
