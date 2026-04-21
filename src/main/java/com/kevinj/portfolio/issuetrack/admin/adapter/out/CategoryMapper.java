package com.kevinj.portfolio.issuetrack.admin.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.domain.CategoryManageInfo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@NoArgsConstructor
public class CategoryMapper {

    public CategoryManageInfo toCategoryMaangeDomain(Category category) {
        if (Objects.isNull(category)) {
            return null;
        }

        return new CategoryManageInfo(
                category.getCategoryId(),
                category.getParentCategory().getCategoryId(),
                category.getLabel(),
                category.getDepth(),
                category.getIsUse(),
                getParentPath(category),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public CategoryManageInfoResponse toCategoryManageInfoResponse(Category category) {
        if (Objects.isNull(category)) {
            return null;
        }

        return new CategoryManageInfoResponse(
                category.getCategoryId(),
                category.getParentCategory().getCategoryId(),
                category.getLabel(),
                category.getDepth(),
                category.getIsUse(),
                getParentPath(category),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public CategoryManageInfoResponse toCategoryManageInfoResponse(CategoryManageInfo category) {
        if (Objects.isNull(category)) {
            return null;
        }

        return new CategoryManageInfoResponse(
                category.getCategoryId(),
                category.getParentCategoryId(),
                category.getLabel(),
                category.getDepth(),
                category.getIsUse(),
                category.getParentPath(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public String getParentPath(Category category) {
        List<String> labels = Stream.iterate(category, Objects::nonNull, Category::getParentCategory)
                .map(Category::getLabel)
                .toList();
        Collections.reverse(labels);
        return String.join(" > ", labels);
    }

    public Category toCategoryEntity(CategoryManageInfo categoryManageInfo, Category parentCategory) {
        return new Category(
                categoryManageInfo.getCategoryId(),
                parentCategory,
                categoryManageInfo.getLabel(),
                categoryManageInfo.getDepth(),
                categoryManageInfo.getIsUse()
        );
    }

}
