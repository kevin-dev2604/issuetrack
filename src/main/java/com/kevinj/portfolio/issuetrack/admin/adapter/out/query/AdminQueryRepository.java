package com.kevinj.portfolio.issuetrack.admin.adapter.out.query;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.CategoryMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Attributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Category;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.QAttributes;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.QCategory;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesSearchQuery;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategoryManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.CategorySearchQuery;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.QIssue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.QIssueAttributes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AdminQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final CategoryMapper categoryMapper;
    private final AttributesMapper attributesMapper;

    public Page<CategoryManageInfoResponse> searchCategory(CategorySearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("depth"), Sort.Order.asc("label"), Sort.Order.desc("createdAt"))
        );

        QCategory category = QCategory.category;

        BooleanBuilder conditions = new BooleanBuilder();

        if (query.label() != null && !query.label().isBlank()) {
            conditions.and(category.label.containsIgnoreCase(query.label()));
        }

        if (query.depth() != null && query.depth() > 0) {
            conditions.and(category.depth.eq(query.depth()));
        }

        if (query.isUse() != null) {
            conditions.and(category.isUse.eq(query.isUse()));
        }

        List<Category> content = queryFactory
                .selectFrom(category)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toCategoryOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(category.count())
                .from(category)
                .where(conditions)
                .fetchOne();

        List<CategoryManageInfoResponse> result = content
                .stream()
                .map(categoryMapper::toCategoryManageInfoResponse)
                .toList();

        return new PageImpl<>(result, pageable, total);
    }

    public Long countSameLabelCategory(Long categoryId, Long parentCategoryId, String label) {
        QCategory category = QCategory.category;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(category.label.eq(label));
        conditions.and(category.parentCategory.categoryId.eq(parentCategoryId));

        if (categoryId != null) {
            conditions.and(category.categoryId.ne(categoryId));
        }

        return queryFactory.selectFrom(category)
                .where(conditions)
                .stream().count();
    }

    public Page<AttributesManageInfoResponse> searchAttributes(AttributesSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("label"), Sort.Order.desc("createdAt"))
        );

        QAttributes attributes = QAttributes.attributes;
        BooleanBuilder conditions = new BooleanBuilder();

        if (query.label() != null && !query.label().isBlank()) {
            conditions.and(attributes.label.containsIgnoreCase(query.label()));
        }

        if (query.isUse() != null) {
            conditions.and(attributes.isUse.eq(query.isUse()));
        }

        List<Attributes> content = queryFactory.selectFrom(attributes)
                .where(conditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toAttributesOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(attributes.count())
                .from(attributes)
                .where(conditions)
                .fetchOne();

        List<AttributesManageInfoResponse> result = content
                .stream()
                .map(attributesMapper::toAttributesManageInfoResponse)
                .toList();

        return new PageImpl<>(result, pageable, total);
    }

    public Long countSameLabelAttributes(Long attributeId, String label) {
        QAttributes attributes = QAttributes.attributes;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(attributes.label.eq(label));

        if (attributeId != null) {
            conditions.and(attributes.attributesId.ne(attributeId));
        }

        return queryFactory.selectFrom(attributes)
                .where(conditions)
                .stream().count();
    }

    public boolean isCategoryUsing(Long categoryId) {
        QIssue issue = QIssue.issue;

        Category category = queryFactory.select(issue.category)
                .from(issue)
                .where(issue.category.categoryId.eq(categoryId))
                .fetchOne();

        return Optional.ofNullable(category).isPresent();
    }

    public boolean isAttributesUsing(Long attributeId) {
        QIssueAttributes issueAttributes = QIssueAttributes.issueAttributes;

        Attributes attributes = queryFactory.select(issueAttributes.attributes)
                .from(issueAttributes)
                .where(issueAttributes.attributes.attributesId.eq(attributeId))
                .fetchOne();

        return Optional.ofNullable(attributes).isPresent();
    }

    private OrderSpecifier<?>[] toCategoryOrderSpecifiers(Sort sort) {
        QCategory categories = QCategory.category;
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "depth" -> orders.add(new OrderSpecifier<>(direction, categories.depth));
                case "label" -> orders.add(new OrderSpecifier<>(direction, categories.label));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, categories.createdAt));
                default -> throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }
    private OrderSpecifier<?>[] toAttributesOrderSpecifiers(Sort sort) {
        QAttributes attributes = QAttributes.attributes;
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "label" -> orders.add(new OrderSpecifier<>(direction, attributes.label));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, attributes.createdAt));
                default -> throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }
}
