package com.kevinj.portfolio.issuetrack.admin.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.*;
import com.kevinj.portfolio.issuetrack.admin.application.port.IssueAdminStatisticsPort;
import com.kevinj.portfolio.issuetrack.issue.adapter.out.query.IssueStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IssueStatisticsPersistenceAdapter implements IssueAdminStatisticsPort {

    private final IssueStatisticsRepository issueStatisticsRepository;

    @Override
    public List<IssueStatusCountRecordResponse> countByStatus() {
        return issueStatisticsRepository.countByStatus();
    }

    @Override
    public List<IssueCategoryCountRecordResponse> countByCategory() {
        return issueStatisticsRepository.countByCategory();
    }

    @Override
    public List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(Integer depth) {
        return issueStatisticsRepository.countByCategoryDepth(depth);
    }

    @Override
    public List<IssueCategoryTreeCountRecordResponse> countByCategoryTree() {
        return issueStatisticsRepository.countByCategoryTree();
    }

    @Override
    public IssueCreateDateCountResponse countCreationByPeriod(IssueCreateDateCountCommand command) {
        if (command.granularity().equals(StatDateUnit.DAY)) {
            return issueStatisticsRepository.countByDayBetween(command.timezone(), command.from(), command.to());
        } else if (command.granularity().equals(StatDateUnit.WEEK)) {
            return issueStatisticsRepository.countByWeekBetween(command.timezone(), command.from(), command.to());
        } else if (command.granularity().equals(StatDateUnit.MONTH)) {
            return issueStatisticsRepository.countByMonthBetween(command.timezone(), command.from(), command.to());
        } else {
            return null;
        }
    }

    @Override
    public List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio() {
        return issueStatisticsRepository.countCategoryIssueDilemmaRatio();
    }
}
