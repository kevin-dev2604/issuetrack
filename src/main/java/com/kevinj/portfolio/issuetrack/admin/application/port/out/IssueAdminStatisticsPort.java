package com.kevinj.portfolio.issuetrack.admin.application.port.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.statistics.*;

import java.util.List;

public interface IssueAdminStatisticsPort {
    List<IssueStatusCountRecordResponse> countByStatus();
    List<IssueCategoryCountRecordResponse> countByCategory();
    List<IssueCategoryDepthCountRecordResponse> countByCategoryDepth(Integer depth);
    List<IssueCategoryTreeCountRecordResponse> countByCategoryTree();
    IssueCreateDateCountResponse countCreationByPeriod(IssueCreateDateCountCommand createDateCountCommand);
    List<IssueDilemmaCategoryOneDepthRatioRecordResponse> countCategoryIssueDilemmaRatio();
}
