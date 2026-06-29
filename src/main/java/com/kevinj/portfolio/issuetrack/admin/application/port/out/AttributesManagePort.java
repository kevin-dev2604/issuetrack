package com.kevinj.portfolio.issuetrack.admin.application.port.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesSearchQuery;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.domain.model.AttributesManageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AttributesManagePort {
    Page<AttributesManageInfoResponse> searchList(AttributesSearchQuery query);
    Optional<AttributesManageInfo> getAttributes(Long attributeId);
    void addAttributes(AttributesCreateCommand command);
    void updateAttributes(AttributesUpdateCommand command);
    void deleteAttributes(Long attributeId);
    boolean isLabelUsing(Long attributeId, String label);
}
