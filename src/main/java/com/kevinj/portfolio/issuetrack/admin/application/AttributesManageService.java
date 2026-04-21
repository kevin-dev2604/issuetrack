package com.kevinj.portfolio.issuetrack.admin.application;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesSearchQuery;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.application.port.AdminUsageCheckPort;
import com.kevinj.portfolio.issuetrack.admin.application.port.AttributesManagePort;
import com.kevinj.portfolio.issuetrack.admin.exception.AttributesAlreadyInUseException;
import com.kevinj.portfolio.issuetrack.admin.exception.DuplicateAttributesException;
import com.kevinj.portfolio.issuetrack.admin.exception.NotFoundAttributesException;
import com.kevinj.portfolio.issuetrack.admin.exception.WrongParametersInputException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttributesManageService implements AttributesManageUseCase{

    private final AttributesManagePort attributesManagePort;
    private final AdminUsageCheckPort adminUsageCheckPort;
    private final AttributesMapper attributesMapper;

    @Override
    public Page<AttributesManageInfoResponse> searchAttributeManageInfo(AttributesSearchQuery query) {
        return attributesManagePort.searchList(query);
    }

    @Override
    public AttributesManageInfoResponse getAttributesManageInfo(Long attributeId) {
        return attributesManagePort.getAttributes(attributeId)
                .map(attributesMapper::toAttributesManageInfoResponse)
                .orElseThrow(NotFoundAttributesException::new);
    }

    @Override
    public void createAttributeManage(AttributesCreateCommand command) {
        if (!validateAttributesInfo(command.label(), command.isUse())) {
            throw new WrongParametersInputException();
        } else if (attributesManagePort.isLabelUsing(null, command.label())) {
            throw new DuplicateAttributesException();
        }

        attributesManagePort.addAttributes(command);
    }

    @Override
    public void updateAttributeManage(AttributesUpdateCommand command) {
        if (!validateAttributesInfo(command.label(), command.isUse())) {
            throw new WrongParametersInputException();
        } else if (attributesManagePort.getAttributes(command.attributesId()).isEmpty()) {
            throw new NotFoundAttributesException();
        } else if (attributesManagePort.isLabelUsing(command.attributesId(), command.label())) {
            throw new DuplicateAttributesException();
        } else if (adminUsageCheckPort.isAttributesUsing(command.attributesId())) {
            throw new AttributesAlreadyInUseException();
        }

        attributesManagePort.updateAttributes(command);
    }

    @Override
    public void deleteAttributeManage(Long attributeId) {
        if (attributesManagePort.getAttributes(attributeId).isEmpty()) {
            throw new NotFoundAttributesException();
        } else if (adminUsageCheckPort.isAttributesUsing(attributeId)) {
            throw new AttributesAlreadyInUseException();
        }

        attributesManagePort.deleteAttributes(attributeId);
    }

    private boolean validateAttributesInfo(String label, YN isUse) {
        if (label == null || label.isBlank() || isUse == null) {
            return false;
        }

        return true;
    }
}
