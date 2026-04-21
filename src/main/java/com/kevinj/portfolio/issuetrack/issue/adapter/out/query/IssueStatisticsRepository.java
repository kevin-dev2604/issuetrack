package com.kevinj.portfolio.issuetrack.issue.adapter.out.query;

import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.*;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeFormats;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeUtils;
import com.kevinj.portfolio.issuetrack.issue.domain.IssueStatus;
import lombok.RequiredArgsConstructor;
import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.example.jooq.tables.Category.CATEGORY;
import static com.example.jooq.tables.Issue.ISSUE;
import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class IssueStatisticsRepository {

    private final DSLContext dsl;

    public List<IssueStatusCountRecordResponse> countByStatus() {
        Field<BigDecimal> totalCount =
            dsl.selectCount()
                    .from(ISSUE)
                    .where(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
                    .asField()
                    .cast(BigDecimal.class);

        return dsl.select(
                    ISSUE.STATUS,
                    count().as("cnt"),
                    round(
                            count().cast(BigDecimal.class)
                                    .divide(totalCount)
                                    .multiply(val(100.0)),
                            2
                    ).as("ratio")
                )
                .from(ISSUE)
                .where(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
                .groupBy(ISSUE.STATUS)
                .fetchInto(IssueStatusCountRecordResponse.class);
    }

    public List<IssueCategoryCountRecordResponse> countByCategory() {
        Field<BigDecimal> totalCount =
                dsl.selectCount()
                        .from(ISSUE)
                        .where(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
                        .asField()
                        .cast(BigDecimal.class);

        return dsl
                .select(
                        CATEGORY.LABEL.as("label"),
                        count(),
                        round(
                                count().cast(BigDecimal.class)
                                        .divide(totalCount)
                                        .multiply(val(100.0)),
                                2
                        ).as("ratio")
                )
                .from(ISSUE.as("i"))
                .join(CATEGORY.as("c")).on(ISSUE.category().CATEGORY_ID.eq(CATEGORY.CATEGORY_ID))
                .where(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
                .and(CATEGORY.IS_USE.eq(val(YN.Y.name())))
                .groupBy(CATEGORY.CATEGORY_ID, CATEGORY.LABEL)
                .fetchInto(IssueCategoryCountRecordResponse.class);

    }

    public List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(Integer depth) {
        if (depth == null || depth <= 0) {
            throw new IllegalArgumentException();
        }

        CommonTableExpression<?> recursiveTable = name("category_map")
            .fields("leaf_id", "group_id", "depth")
            .as(
                select(
                    CATEGORY.CATEGORY_ID.as("leaf_id"),
                    CATEGORY.CATEGORY_ID.as("group_id"),
                    CATEGORY.DEPTH
                )
                .from(CATEGORY)
                .where(CATEGORY.DEPTH.eq(depth))
                .unionAll(
                    select(
                        CATEGORY.CATEGORY_ID.as("leaf_id"),
                        field(name("m", "group_id"), SQLDataType.BIGINT),
                        CATEGORY.DEPTH
                    )
                    .from(CATEGORY)
                    .join(table(name("category_map")).as("m"))
                    .on(CATEGORY.PARENT_CATEGORY.eq(field(name("m", "leaf_id"), SQLDataType.BIGINT)))
                )
            );

        return dsl.withRecursive(recursiveTable)
            .select(
                field(name("m", "group_id"), SQLDataType.BIGINT),
                CATEGORY.LABEL,
                count(ISSUE.ISSUE_ID).as("cnt")
            )
            .from(ISSUE)
            .join(recursiveTable.as("m"))
            .on(ISSUE.CATEGORY.eq(
                field(name("m", "leaf_id"), SQLDataType.BIGINT))
            )
            .join(CATEGORY)
            .on(field(name("m", "group_id"), SQLDataType.BIGINT).eq(CATEGORY.CATEGORY_ID))
            .where(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
            .groupBy(
                field(name("m", "group_id"), SQLDataType.BIGINT),
                CATEGORY.LABEL
            )
            .orderBy(field(name("m", "group_id"), SQLDataType.BIGINT))
            .fetchInto(IssueCategoryDepthCountRecordResponse.class);

    }

    public List<IssueCategoryTreeCountRecordResponse> countByCategoryTree() {
        CommonTableExpression<?> recursiveTable = name("category_tree")
            .fields("category_id", "group_id", "parent_category", "depth", "label", "sort_path")
            .as(
                select(
                    CATEGORY.CATEGORY_ID,
                    CATEGORY.CATEGORY_ID.as("group_id"),
                    CATEGORY.PARENT_CATEGORY,
                    CATEGORY.DEPTH,
                    CATEGORY.LABEL,
                    lpad(CATEGORY.CATEGORY_ID.cast(SQLDataType.VARCHAR), 4, "0")
                        .cast(SQLDataType.VARCHAR).as("sort_path")
                )
                .from(CATEGORY)
                .where(CATEGORY.PARENT_CATEGORY.isNull())
                .unionAll(
                    select(
                        CATEGORY.CATEGORY_ID,
                        field(name("ct", "group_id"), SQLDataType.BIGINT),
                        CATEGORY.PARENT_CATEGORY,
                        CATEGORY.DEPTH,
                        CATEGORY.LABEL,
                        concat(
                            field(name("ct", "sort_path"), SQLDataType.VARCHAR),
                            val("-"),
                            lpad(CATEGORY.CATEGORY_ID.cast(SQLDataType.VARCHAR), 4, "0")
                                .cast(SQLDataType.VARCHAR)
                        ).as("sort_path")
                    )
                    .from(CATEGORY)
                    .join(table(name("category_tree")).as("ct"))
                    .on(CATEGORY.PARENT_CATEGORY.eq(
                        field(name("ct", "category_id"), SQLDataType.BIGINT)
                    ))
                )
            );

        return dsl.withRecursive(recursiveTable)
            .select(
                field(name("ct", "group_id"), SQLDataType.BIGINT),
                field(name("ct", "label"), SQLDataType.VARCHAR),
                field(name("ct", "depth"), SQLDataType.INTEGER),
                count(field(name("i", "issue_id"), SQLDataType.BIGINT)).as("cnt")
            )
            .from(ISSUE.as("i"))
            .join(recursiveTable.as("ct"))
            .on(field(name("i", "category"), SQLDataType.BIGINT).eq(
                field(name("ct", "category_id"), SQLDataType.BIGINT))
            )
            .join(CATEGORY.as("c"))
            .on(field(name("ct", "group_id"), SQLDataType.BIGINT).eq(
                field(name("c", "category_id"), SQLDataType.BIGINT))
            )
            .where(field(name("i", "status"), SQLDataType.VARCHAR).ne(IssueStatus.DELETED.name()))
            .groupBy(
                field(name("ct", "group_id"), SQLDataType.BIGINT),
                field(name("ct", "label"), SQLDataType.VARCHAR),
                field(name("ct", "depth"), SQLDataType.INTEGER),
                field(name("ct", "sort_path"), SQLDataType.VARCHAR)
            )
            .orderBy(field(name("ct", "sort_path"), SQLDataType.VARCHAR))
            .fetchInto(IssueCategoryTreeCountRecordResponse.class);
    }

    public IssueCreateDateCountResponse countByDayBetween(String timezone, String from, String to) {
        ZoneId zoneId = ZoneId.systemDefault();

        if (timezone != null && !timezone.isBlank()) {
            zoneId = ZoneId.of(timezone);
        }

        LocalDateTime fromWithTimezone = DateTimeUtils.parseDateTime(from, zoneId);
        LocalDateTime toWithTimezone = DateTimeUtils.parseDateTime(to, zoneId).plusDays(1L);

        Field<LocalDateTime> createdAtWithTimezone =
            DSL.field(
                "{0} AT TIME ZONE {1}",
                SQLDataType.LOCALDATETIME,
                ISSUE.CREATED_AT,
                DSL.inline(timezone)
            );

        List<IssueCreateDateCountBucket> buckets = dsl.select(
                toChar(createdAtWithTimezone, DateTimeFormats.DEFAULT_DATE.toUpperCase()).as("bucket"),
                count()
            )
            .from(ISSUE)
            .where(createdAtWithTimezone.ge(val(fromWithTimezone)))
            .and(createdAtWithTimezone.lessThan(toWithTimezone))
            .and(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
            .groupBy(field(name("bucket")))
            .orderBy(field(name("bucket")))
            .fetchInto(IssueCreateDateCountBucket.class)
            ;

        return new IssueCreateDateCountResponse(
            StatDateUnit.DAY,
            timezone,
            buckets
        );
    }

    public IssueCreateDateCountResponse countByWeekBetween(String timezone, String from, String to) {
        Field<LocalDateTime> createdAtWithTimezone =
            DSL.field(
                "{0} AT TIME ZONE {1}",
                SQLDataType.LOCALDATETIME,
                ISSUE.CREATED_AT,
                DSL.inline(timezone)
            );

        List<IssueCreateDateCountBucket> buckets = dsl.select(
                toChar(createdAtWithTimezone, DateTimeFormats.POSTGRE_WEEK).as("bucket"),
                count()
            )
            .from(ISSUE)
            .where(
                toChar(createdAtWithTimezone, DateTimeFormats.POSTGRE_WEEK)
                    .between(from, to)
            )
            .and(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
            .groupBy(field(name("bucket")))
            .orderBy(field(name("bucket")))
            .fetchInto(IssueCreateDateCountBucket.class)
            ;

        return new IssueCreateDateCountResponse(
            StatDateUnit.WEEK,
            timezone,
            buckets
        );
    }

    public IssueCreateDateCountResponse countByMonthBetween(String timezone, String from, String to) {
        Field<LocalDateTime> createdAtWithTimezone =
            DSL.field(
                "{0} AT TIME ZONE {1}",
                SQLDataType.LOCALDATETIME,
                ISSUE.CREATED_AT,
                DSL.inline(timezone)
            );

        List<IssueCreateDateCountBucket> buckets = dsl.select(
                toChar(createdAtWithTimezone, DateTimeFormats.DEFAULT_MONTH.toUpperCase()).as("bucket"),
                count()
            )
            .from(ISSUE)
            .where(
                toChar(createdAtWithTimezone, DateTimeFormats.DEFAULT_MONTH.toUpperCase())
                    .between(from, to)
            )
            .and(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
            .groupBy(field(name("bucket")))
            .orderBy(field(name("bucket")))
            .fetchInto(IssueCreateDateCountBucket.class)
            ;

        return new IssueCreateDateCountResponse(
            StatDateUnit.MONTH,
            timezone,
            buckets
        );
    }

    public List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio() {

        CommonTableExpression<?> recursiveTable = name("category_map")
            .fields("leaf_id", "group_id")
            .as(
                select(
                    CATEGORY.CATEGORY_ID.as("leaf_id"),
                    CATEGORY.CATEGORY_ID.as("group_id")
                )
                    .from(CATEGORY)
                    .where(CATEGORY.DEPTH.eq(1))
                    .unionAll(
                        select(
                            CATEGORY.CATEGORY_ID.as("leaf_id"),
                            field(name("m", "group_id"), SQLDataType.BIGINT)
                        )
                        .from(CATEGORY)
                        .join(table(name("category_map")).as("m"))
                        .on(CATEGORY.PARENT_CATEGORY.eq(field(name("m", "leaf_id"), SQLDataType.BIGINT)))
                    )
            );

        return dsl.withRecursive(recursiveTable)
            .select(
                field(name("m", "group_id"), SQLDataType.BIGINT),
                CATEGORY.LABEL,
                coalesce(
                    val(100.0)
                        .multiply(count(ISSUE.ISSUE_ID).filterWhere(ISSUE.STATUS.equal(IssueStatus.DILEMMA.name())))
                        .divide(nullif(count(ISSUE.ISSUE_ID), 0))
                ).as("dilemma_ratio")
            )
            .from(recursiveTable.as("m"))
            .join(CATEGORY)
            .on(field(name("m", "group_id"), SQLDataType.BIGINT).eq(CATEGORY.CATEGORY_ID))
            .leftJoin(ISSUE)
            .on(ISSUE.CATEGORY.eq(
                field(name("m", "leaf_id"), SQLDataType.BIGINT))
            )
            .and(ISSUE.STATUS.ne(IssueStatus.DELETED.name()))
            .groupBy(
                field(name("m", "group_id"), SQLDataType.BIGINT),
                CATEGORY.LABEL
            )
            .orderBy(field(name("m", "group_id"), SQLDataType.BIGINT))
            .fetchInto(IssueDilemmaCategoryOneDepthRatioRecordResponse.class);
    }
}