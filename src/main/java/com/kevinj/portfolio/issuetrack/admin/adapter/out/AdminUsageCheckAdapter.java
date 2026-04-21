package com.kevinj.portfolio.issuetrack.admin.adapter.out;

import com.kevinj.portfolio.issuetrack.admin.adapter.out.query.AdminQueryRepository;
import com.kevinj.portfolio.issuetrack.admin.application.port.AdminUsageCheckPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUsageCheckAdapter implements AdminUsageCheckPort {

    private final AdminQueryRepository adminQueryRepository;

    @Override
    public boolean isCategoryUsing(Long categoryId) {
        return adminQueryRepository.isCategoryUsing(categoryId);
    }

    @Override
    public boolean isAttributesUsing(Long attributeId) {
        return adminQueryRepository.isAttributesUsing(attributeId);
    }
}
