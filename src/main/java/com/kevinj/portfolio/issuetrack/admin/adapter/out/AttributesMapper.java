package com.kevinj.portfolio.issuetrack.admin.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.Attributes;
import com.kevinj.portfolio.issuetrack.admin.application.dto.AttributesManageInfoResponse;
import com.kevinj.portfolio.issuetrack.admin.domain.AttributesManageInfo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class AttributesMapper {

    public AttributesManageInfo toAttributesMaangeDomain(Attributes attributes) {
        if (Objects.isNull(attributes)) {
            return null;
        }

        return new AttributesManageInfo(
                attributes.getAttributesId(),
                attributes.getLabel(),
                attributes.getIsUse(),
                attributes.getCreatedAt(),
                attributes.getUpdatedAt()
        );
    }

    public AttributesManageInfoResponse toAttributesManageInfoResponse(Attributes attributes) {
        if (Objects.isNull(attributes)) {
            return null;
        }

        return new AttributesManageInfoResponse(
                attributes.getAttributesId(),
                attributes.getLabel(),
                attributes.getIsUse(),
                attributes.getCreatedAt(),
                attributes.getUpdatedAt()
        );
    }

    public AttributesManageInfoResponse toAttributesManageInfoResponse(AttributesManageInfo attributesInfo) {
        if (Objects.isNull(attributesInfo)) {
            return null;
        }

        return new AttributesManageInfoResponse(
                attributesInfo.getAttributesId(),
                attributesInfo.getLabel(),
                attributesInfo.getIsUse(),
                attributesInfo.getCreatedAt(),
                attributesInfo.getUpdatedAt()
        );
    }

    public Attributes toEntity(AttributesManageInfo attributesInfo) {
        return new Attributes(
                attributesInfo.getAttributesId(),
                attributesInfo.getLabel(),
                attributesInfo.getIsUse()
        );
    }

}
