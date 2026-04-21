package com.kevinj.portfolio.issuetrack.dilemma.adapter.out.query;

import com.kevinj.portfolio.issuetrack.dilemma.adapter.out.jpa.QDilemma;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchQuery;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaSearchResponse;
import com.kevinj.portfolio.issuetrack.dilemma.application.dto.DilemmaUserSearchQuery;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DilemmaQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<DilemmaSearchResponse> searchUserDilemma(Users user, DilemmaUserSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        QDilemma dilemma = QDilemma.dilemma;

        List<DilemmaSearchResponse> result = queryFactory
                .select(
                        Projections.constructor(DilemmaSearchResponse.class,
                                dilemma.dilemmaId,
                                dilemma.title,
                                dilemma.issue.issueId,
                                dilemma.issue.title,
                                dilemma.isOpen,
                                dilemma.createdAt,
                                dilemma.updatedAt
                        )
                )
                .from(dilemma)
                .where(
                        userEq(user),
                        containsKeyword(query.keyword()),
                        createdAtBetween(query.fromDate(), query.toDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toDilemmaOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(dilemma.count())
                .from(dilemma)
                .where(
                        userEq(user),
                        containsKeyword(query.keyword()),
                        createdAtBetween(query.fromDate(), query.toDate())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    public Page<DilemmaSearchResponse> searchDilemma(DilemmaSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Direction.fromString(query.direction().toUpperCase()), query.sortBy())
        );

        QDilemma dilemma = QDilemma.dilemma;

        List<DilemmaSearchResponse> result = queryFactory
                .select(
                        Projections.constructor(DilemmaSearchResponse.class,
                                dilemma.dilemmaId,
                                dilemma.title,
                                dilemma.issue.issueId,
                                dilemma.issue.title,
                                dilemma.isOpen,
                                dilemma.createdAt,
                                dilemma.updatedAt
                        )
                )
                .from(dilemma)
                .where(
                        nicknameContains(query.nickname()),
                        categoryIdEq(query.categoryId()),
                        processIdEq(query.processId()),
                        titleContains(query.title()),
                        detailsContains(query.details()),
                        createdAtBetween(query.createFrom(), query.createTo()),
                        updatedAtBetween(query.updateFrom(), query.updateTo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toDilemmaOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(dilemma.count())
                .from(dilemma)
                .where(
                        nicknameContains(query.nickname()),
                        categoryIdEq(query.categoryId()),
                        processIdEq(query.processId()),
                        titleContains(query.title()),
                        detailsContains(query.details()),
                        createdAtBetween(query.createFrom(), query.createTo()),
                        updatedAtBetween(query.updateFrom(), query.updateTo())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression userEq(Users user) {
        return QDilemma.dilemma.issue.user.eq(user);
    }

    private BooleanExpression containsKeyword(String keyword) {
        QDilemma dilemma = QDilemma.dilemma;

        return (keyword == null || keyword.isBlank())
                ? null
                : dilemma.title.containsIgnoreCase(keyword).or(dilemma.details.containsIgnoreCase(keyword));
    }

    private BooleanBuilder createdAtBetween(LocalDateTime from, LocalDateTime to) {
        QDilemma dilemma = QDilemma.dilemma;

        BooleanBuilder condition = new BooleanBuilder();

        if (from != null) {
            condition.and(dilemma.createdAt.goe(from));
        }
        if (to != null) {
            condition.and(dilemma.createdAt.loe(to));
        }
        return condition;
    }

    private BooleanExpression nicknameContains(String nickname) {
        QDilemma dilemma = QDilemma.dilemma;

        return (nickname == null || nickname.isBlank())
                ? null
                : dilemma.issue.user.nickname.containsIgnoreCase(nickname);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        QDilemma dilemma = QDilemma.dilemma;

        return categoryId == null
                ? null
                : dilemma.issue.category.categoryId.eq(categoryId);
    }

    private BooleanExpression processIdEq(Long processId) {
        QDilemma dilemma = QDilemma.dilemma;

        return processId == null
                ? null
                : dilemma.issue.process.processId.eq(processId);
    }

    private BooleanExpression titleContains(String title) {
        QDilemma dilemma = QDilemma.dilemma;

        return (title == null || title.isBlank())
                ? null
                : dilemma.title.containsIgnoreCase(title);
    }

    private BooleanExpression detailsContains(String details) {
        QDilemma dilemma = QDilemma.dilemma;

        return (details == null || details.isBlank())
                ? null
                : dilemma.details.containsIgnoreCase(details);
    }

    private BooleanBuilder updatedAtBetween(LocalDateTime from, LocalDateTime to) {
        QDilemma dilemma = QDilemma.dilemma;

        BooleanBuilder condition = new BooleanBuilder();

        if (from != null) {
            condition.and(dilemma.updatedAt.goe(from));
        }
        if (to != null) {
            condition.and(dilemma.updatedAt.loe(to));
        }
        return condition;
    }

    private OrderSpecifier<?>[] toDilemmaOrderSpecifiers(Sort sort) {
        QDilemma dilemma = QDilemma.dilemma;
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "issue" -> orders.add(new OrderSpecifier<>(direction, dilemma.issue.title));
                case "title" -> orders.add(new OrderSpecifier<>(direction, dilemma.title));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, dilemma.createdAt));
                default -> throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }
}
