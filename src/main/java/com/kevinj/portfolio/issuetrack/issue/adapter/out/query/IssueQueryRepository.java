package com.kevinj.portfolio.issuetrack.issue.adapter.out.query;

import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.QIssue;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.QIssueAttributes;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueAttributesResponseInfo;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchQuery;
import com.kevinj.portfolio.issuetrack.issue.application.dto.IssueSearchResponse;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class IssueQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<IssueSearchResponse> searchIssues(Users user, IssueSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        QIssue issue = QIssue.issue;

        List<IssueSearchResponse> result = queryFactory
                .select(
                        Projections.constructor(IssueSearchResponse.class,
                                issue.issueId,
                                issue.category.categoryId,
                                issue.category.label,
                                issue.process.processId,
                                issue.process.name,
                                issue.currentStep.stepId,
                                issue.currentStep.name,
                                issue.title,
                                issue.createdAt,
                                issue.updatedAt
                        )
                )
                .from(issue)
                .where(
                        userEq(user),
                        categoryIdEq(query.categoryId()),
                        processIdEq(query.processId()),
                        titleContains(query.title()),
                        detailContains(query.details()),
                        notDeleted()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toIssueOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(issue.count())
                .from(issue)
                .where(
                        categoryIdEq(query.categoryId()),
                        processIdEq(query.processId()),
                        titleContains(query.title()),
                        detailContains(query.details())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    public List<IssueAttributesResponseInfo> getIssueAttributesDisplayList(Long issueId) {
        QIssueAttributes issueAttributes = QIssueAttributes.issueAttributes;

        return queryFactory
                .select(
                        Projections.constructor(IssueAttributesResponseInfo.class,
                                issueAttributes.id,
                                issueAttributes.attributes.attributesId,
                                issueAttributes.attributes.label,
                                issueAttributes.value
                        )
                )
                .from(issueAttributes)
                .where(issueAttributes.issue.issueId.eq(issueId))
                .orderBy(issueAttributes.attributes.label.asc())
                .fetch();
    }

    private BooleanExpression userEq(Users user) {
        QIssue issue = QIssue.issue;
        return user == null ? null : issue.user.eq(user);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        QIssue issue = QIssue.issue;
        return categoryId == null ? null : issue.category.categoryId.eq(categoryId);
    }

    private BooleanExpression processIdEq(Long processId) {
        QIssue issue = QIssue.issue;
        return processId == null ? null : issue.process.processId.eq(processId);
    }

    private BooleanExpression titleContains(String title) {
        QIssue issue = QIssue.issue;
        return (title == null || title.isBlank()) ? null : issue.title.contains(title);
    }

    private BooleanExpression detailContains(String details) {
        QIssue issue = QIssue.issue;
        return (details == null || details.isBlank()) ? null : issue.details.contains(details);
    }

    private BooleanExpression notDeleted() {
        QIssue issue = QIssue.issue;
        return issue.status.ne(IssueStatus.DELETED);
    }

    private OrderSpecifier<?>[] toIssueOrderSpecifiers(Sort sort) {
        QIssue issue = QIssue.issue;
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "category" -> orders.add(new OrderSpecifier<>(direction, issue.category.label));
                case "process" -> orders.add(new OrderSpecifier<>(direction, issue.process.name));
                case "title" -> orders.add(new OrderSpecifier<>(direction, issue.title));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, issue.createdAt));
                default -> throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }

}
