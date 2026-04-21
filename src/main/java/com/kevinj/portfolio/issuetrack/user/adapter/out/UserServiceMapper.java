package com.kevinj.portfolio.issuetrack.user.adapter.out;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.ServiceUsage;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.ServiceUsageId;
import com.kevinj.portfolio.issuetrack.user.domain.ServiceDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserServiceMapper {

    @Getter
    private final String serviceName = "issuetrack";

    public ServiceUsage toServiceUsageEntity(Long userId) {
        ServiceUsageId idKey = new ServiceUsageId(userId, serviceName);
        return new ServiceUsage(idKey, YN.Y);
    }

    public ServiceDomain toUserServiceDomain(ServiceUsage service) {
        ServiceUsageId serviceUsageId = service.getId();
        return new ServiceDomain(
                serviceUsageId.getUserId(),
                serviceUsageId.getService(),
                service.getIsUse()
        );
    }
}
