package com.kevinj.portfolio.issuetrack.process.adapter.out.query;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Process;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.QStep;
import com.kevinj.portfolio.issuetrack.process.adapter.out.jpa.Step;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StepQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Step> getAllStep(Process process) {
        QStep step = QStep.step;

        return queryFactory
                .selectFrom(step)
                .where(
                        processEq(process),
                        isNotDeleted()
                )
                .orderBy(step.order.asc())
                .fetch();
    }

    public List<Step> getActiveStep(Process process) {
        QStep step = QStep.step;

        return queryFactory
                .selectFrom(step)
                .where(
                        processEq(process),
                        isActiveStep(),
                        isNotDeleted()
                )
                .orderBy(step.order.asc())
                .fetch();
    }

    public Optional<Step> getStepUnscoped(Long processId, Long stepId) {
        QStep step = QStep.step;

        Step entity = queryFactory
                .selectFrom(step)
                .where(
                        step.stepId.eq(stepId),
                        step.process.processId.eq(processId)
                )
                .fetchOne();

        return Optional.ofNullable(entity);
    }

    public Optional<Step> getInitialStep(Process process) {
        QStep step = QStep.step;

        Step entity = queryFactory
                .selectFrom(step)
                .where(
                        processEq(process),
                        isActiveStep(),
                        isNotDeleted()
                )
                .orderBy(step.order.asc())
                .fetchFirst();

        return Optional.ofNullable(entity);
    }

    public Optional<Step> getNextStep(Process process, Integer order) {
        QStep step = QStep.step;

        Step entity = queryFactory
                .selectFrom(step)
                .where(
                        processEq(process),
                        isActiveStep(),
                        isNotDeleted(),
                        orderGt(order)
                )
                .orderBy(step.order.asc())
                .fetchFirst();

        return Optional.ofNullable(entity);
    }

    private BooleanExpression processEq(Process process) {
        QStep step = QStep.step;
        return step.process.eq(process);
    }

    private BooleanExpression isActiveStep() {
        QStep step = QStep.step;
        return step.isActive.isNotNull().and(step.isActive.eq(YN.Y));
    }

    private BooleanExpression isNotDeleted() {
        QStep step = QStep.step;
        return step.isDeleted.isNull().or(step.isDeleted.eq(YN.N));
    }

    private BooleanExpression orderGt(Integer order) {
        QStep step = QStep.step;
        return step.order.gt(order);
    }
}
