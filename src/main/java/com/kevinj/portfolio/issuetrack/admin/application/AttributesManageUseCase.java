package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.application.dto.*;
import org.springframework.data.domain.Page;

public interface AttributesManageUseCase {
    Page<AttributesManageInfoResponse> searchAttributeManageInfo(AttributesSearchQuery query);
    AttributesManageInfoResponse getAttributesManageInfo(Long attributeId);
    void createAttributeManage(AttributesCreateCommand command);
    void updateAttributeManage(AttributesUpdateCommand command);
    void deleteAttributeManage(Long attributeId);
}
