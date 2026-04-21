package com.kevinj.portfolio.issuetrack.process.adapter.out.query;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.jpa.QIssue;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import com.kevinj.portfolio.issuetrack.process.adapter.out.ProcessAndStepMapper;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.QProcess;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessInfoResponse;
import com.kevinj.portfolio.issuetrack.process.application.dto.process.ProcessSearchQuery;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProcessQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final ProcessAndStepMapper processAndStepMapper;

    public Page<ProcessInfoResponse> searchProcess(Users user, ProcessSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("name"), Sort.Order.desc("createdAt"))
        );

        QProcess process = QProcess.process;

        List<Process> content = queryFactory
                .selectFrom(process)
                .where(
                        userEq(user),
                        nameContains(query.name()),
                        isActiveEq(query.isActive())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(toOrderSpecifiers(pageable.getSort()))
                .fetch();

        Long total = queryFactory
                .select(process.count())
                .from(process)
                .where(
                        userEq(user),
                        nameContains(query.name()),
                        isActiveEq(query.isActive()),
                        isNotDeleted()
                )
                .fetchOne();

        List<ProcessInfoResponse> result = content
                .stream()
                .map(processAndStepMapper::toProcessInfoResponse)
                .toList();

        return new PageImpl<>(result, pageable, total);
    }

    public Optional<Process> getProcess(Users user, Long processId) {
        QProcess process = QProcess.process;

        Process entity = queryFactory
                .selectFrom(process)
                .from(process)
                .where(
                        userEq(user),
                        processIdEq(processId),
                        isNotDeleted()
                )
                .fetchOne();

        return Optional.ofNullable(entity);

    }

    public boolean isProcessUsing(Users user, Long processId) {
        QIssue issue = QIssue.issue;

        Process process = queryFactory
                .select(issue.process)
                .from(issue)
                .where(
                        issue.user.eq(user),
                        issue.process.processId.eq(processId),
                        issue.status.notIn(IssueStatus.EXIT, IssueStatus.DELETED)
                )
                .fetchOne();

        return Optional.ofNullable(process).isPresent();

    }

    private BooleanExpression userEq(Users user) {
        QProcess process = QProcess.process;
        return process.user.eq(user);
    }

    private BooleanExpression nameContains(String keyword) {
        QProcess process = QProcess.process;
        return (keyword == null || keyword.isBlank()) ? null : process.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression isActiveEq(YN isActive) {
        QProcess process = QProcess.process;
        return isActive == null ? null : process.isActive.eq(isActive);
    }

    private BooleanExpression processIdEq(Long processId) {
        QProcess process = QProcess.process;
        return process.processId.eq(processId);
    }

    private BooleanExpression isNotDeleted() {
        QProcess process = QProcess.process;
        return process.isDeleted.isNull().or(process.isDeleted.eq(YN.N));
    }

    private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {
        QProcess process = QProcess.process;
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "name" -> orders.add(new OrderSpecifier<>(direction, process.name));
                case "createdAt" -> orders.add(new OrderSpecifier<>(direction, process.createdAt));
                default -> throw new IllegalArgumentException("Unsupported sort property: " + order.getProperty());
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }
}
