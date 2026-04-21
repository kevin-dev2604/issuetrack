package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.application.dto.statistics.*;

import java.util.List;

public interface IssueStatisticsUseCase {
    List<IssueStatusCountRecordResponse> countByStatus();
    List<IssueCategoryCountRecordResponse> countByCategory();
    List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(Integer depth);
    List<IssueCategoryTreeCountRecordResponse> countByCategoryTree();
    IssueCreateDateCountResponse countCreationByPeriod(IssueCreateDateCountCommand createDateCountCommand);
    List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio();
}
