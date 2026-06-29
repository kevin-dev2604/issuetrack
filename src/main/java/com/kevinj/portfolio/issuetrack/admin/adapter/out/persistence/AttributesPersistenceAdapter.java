package com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence;

import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesCreateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesSearchQuery;
import com.kevinj.portfolio.issuetrack.admin.adapter.in.web.dto.AttributesUpdateCommand;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.persistence.query.AdminQueryRepository;
import com.kevinj.portfolio.issuetrack.admin.application.port.out.AttributesManagePort;
import com.kevinj.portfolio.issuetrack.admin.domain.model.AttributesManageInfo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttributesPersistenceAdapter implements AttributesManagePort {

    private final JpaAttributesRepository jpaAttributesRepository;
    private final AdminQueryRepository adminQueryRepository;
    private final AttributesMapper attributesMapper;

    @Override
    public Page<AttributesManageInfoResponse> searchList(AttributesSearchQuery query) {
        return adminQueryRepository.searchAttributes(query);
    }

    @Override
    public Optional<AttributesManageInfo> getAttributes(Long attributeId) {
        return jpaAttributesRepository.findById(attributeId)
                .map(attributesMapper::toAttributesMaangeDomain);
    }

    @Override
    public void addAttributes(AttributesCreateCommand command) {
        Attributes attributes = new Attributes(null, command.label(), command.isUse());
        jpaAttributesRepository.save(attributes);
    }

    @Override
    public void updateAttributes(AttributesUpdateCommand command) {
        AttributesManageInfo domain = jpaAttributesRepository.findById(command.attributesId())
                .map(attributesMapper::toAttributesMaangeDomain)
                .orElseThrow(() -> new EntityNotFoundException("Attributes not found"));

        domain.update(command.label(), command.isUse());

        jpaAttributesRepository.save(attributesMapper.toEntity(domain));
    }

    @Override
    public void deleteAttributes(Long attributeId) {
        Attributes attributes = jpaAttributesRepository.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException("Attributes not found"));

        jpaAttributesRepository.delete(attributes);
    }

    @Override
    public boolean isLabelUsing(Long attributeId, String label) {
        return adminQueryRepository.countSameLabelAttributes(attributeId, label) > 0;
    }
}
