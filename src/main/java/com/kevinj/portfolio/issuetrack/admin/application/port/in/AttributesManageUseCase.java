package com.kevinj.portfolio.issuetrack.admin.application.port.in;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesSearchQuery;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesUpdateCommand;
import org.springframework.data.domain.Page;

public interface AttributesManageUseCase {
    Page<AttributesManageInfoResponse> searchAttributeManageInfo(AttributesSearchQuery query);
    AttributesManageInfoResponse getAttributesManageInfo(Long attributeId);
    void createAttributeManage(AttributesCreateCommand command);
    void updateAttributeManage(AttributesUpdateCommand command);
    void deleteAttributeManage(Long attributeId);
}
