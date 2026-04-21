package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.*;
import com.kevinj.portfolio.issuetrack.admin.application.port.IssueAdminStatisticsPort;
import com.kevinj.portfolio.issuetrack.admin.exception.InvalidPeriodInputException;
import com.kevinj.portfolio.issuetrack.admin.exception.InvalidTimezoneException;
import com.kevinj.portfolio.issuetrack.admin.exception.NonPositiveDepthValueException;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeFormats;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueStatisticsService implements IssueStatisticsUseCase{

    private final IssueAdminStatisticsPort issueAdminStatisticsPort;

    @Override
    public List<IssueStatusCountRecordResponse> countByStatus() {
        return issueAdminStatisticsPort.countByStatus();
    }

    @Override
    public List<IssueCategoryCountRecordResponse> countByCategory() {
        return issueAdminStatisticsPort.countByCategory();
    }

    @Override
    public List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(Integer depth) {
        if (depth == null || depth <= 0) {
            throw new NonPositiveDepthValueException();
        }

        return issueAdminStatisticsPort.countByCategoryDepth(depth);
    }

    @Override
    public List<IssueCategoryTreeCountRecordResponse> countByCategoryTree() {
        return issueAdminStatisticsPort.countByCategoryTree();
    }

    @Override
    public IssueCreateDateCountResponse countCreationByPeriod(IssueCreateDateCountCommand createDateCountCommand) {
        if (isValidTimezone(createDateCountCommand.timezone())) {
            throw new InvalidTimezoneException();
        } else if (isDateStringValid(createDateCountCommand)){
            throw new InvalidPeriodInputException();
        }

        return issueAdminStatisticsPort.countCreationByPeriod(createDateCountCommand);
    }

    @Override
    public List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio() {
        return issueAdminStatisticsPort.countCategoryIssueDilemmaRatio();
    }

    private boolean isValidTimezone(String timezone) {
        if (timezone == null || timezone.isBlank()) {
            return false;
        }

        try {
            ZoneId.of(timezone);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean isDateStringValid(IssueCreateDateCountCommand command) {
        if (command.granularity().equals(StatDateUnit.DAY)) {
            return DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_DATE, command.from())
                &&  DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_DATE, command.to());
        } else if (command.granularity().equals(StatDateUnit.WEEK)) {
            return DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_WEEK, command.from())
                &&  DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_WEEK, command.to());
        } else if (command.granularity().equals(StatDateUnit.MONTH)) {
            return DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_MONTH, command.from())
                &&  DateTimeUtils.isParsable(DateTimeFormats.DEFAULT_MONTH, command.to());
        }

        return false;
    }
}
