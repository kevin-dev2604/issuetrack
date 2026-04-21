package com.kevinj.portfolio.issuetrack.admin;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.admin.adapter.out.AttributesMapper;
import com.kevinj.portfolio.issuetrack.admin.application.dto.*;
import com.kevinj.portfolio.issuetrack.admin.application.port.AttributesManagePort;
import com.kevinj.portfolio.issuetrack.admin.domain.AttributesManageInfo;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import com.kevinj.portfolio.issuetrack.global.time.TimeProvider;
import org.springframework.data.domain.*;

import java.util.*;

public class FakeAttributeManagePort implements AttributesManagePort, FakePort {

    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final Map<Long, AttributesManageInfo> attributesManageInfoList = new HashMap<>();
    private final AttributesMapper attributesMapper;

    public FakeAttributeManagePort(AttributesMapper attributesMapper) {
        this.attributesMapper = attributesMapper;
    }

    @Override
    public Page<AttributesManageInfoResponse> searchList(AttributesSearchQuery query) {
        Pageable pageable = PageRequest.of(
                query.page() - 1,
                query.size(),
                Sort.by(Sort.Order.asc("depth"), Sort.Order.asc("label"), Sort.Order.desc("createdAt"))
        );

        List<AttributesManageInfoResponse> content = attributesManageInfoList.values()
                .stream()
                .filter(attributes -> {
                    Boolean result = true;
                    if (query.label() != null && !query.label().isBlank()) {
                        result &= attributes.getLabel().contains(query.label());
                    }
                    if (query.isUse() != null) {
                        result &= attributes.getIsUse().equals(query.isUse());
                    }
                    return result;
                })
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .map(attributesMapper::toAttributesManageInfoResponse)
                .toList();

        Long total = attributesManageInfoList.values()
                .stream()
                .filter(attributes -> {
                    Boolean result = true;
                    if (query.label() != null && !query.label().isBlank()) {
                        result &= attributes.getLabel().contains(query.label());
                    }
                    if (query.isUse() != null) {
                        result &= attributes.getIsUse().equals(query.isUse());
                    }
                    return result;
                })
                .count();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<AttributesManageInfo> getAttributes(Long attributeId) {
        return Optional.ofNullable(attributesManageInfoList.get(attributeId));
    }

    @Override
    public void addAttributes(AttributesCreateCommand command) {
        Long attributesId = (long) attributesManageInfoList.size() + 1;
        attributesManageInfoList.put(attributesId, new AttributesManageInfo(
                attributesId,
                command.label(),
                command.isUse(),
                timeProvider.now(),
                timeProvider.now()
        ));
    }

    @Override
    public void updateAttributes(AttributesUpdateCommand command) {
        AttributesManageInfo attributesManageInfo = attributesManageInfoList.get(command.attributesId());
        attributesManageInfo.update(command.label(), command.isUse());
    }

    @Override
    public void deleteAttributes(Long attributeId) {
        attributesManageInfoList.remove(attributeId);
    }

    @Override
    public boolean isLabelUsing(Long attributeId, String label) {
        return attributesManageInfoList.values()
                .stream()
                .anyMatch(attributes ->
                        !Objects.equals(attributes.getAttributesId(), attributeId) && attributes.getLabel().equals(label)
                );
    }

    @Override
    public Long newId() {
        return (long) attributesManageInfoList.size() + 1;
    }

    @Override
    public Long lastId() {
        return attributesManageInfoList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
