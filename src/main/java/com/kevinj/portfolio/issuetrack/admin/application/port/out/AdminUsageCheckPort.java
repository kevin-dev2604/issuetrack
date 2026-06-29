package com.kevinj.portfolio.issuetrack.admin.application.port.out;

public interface AdminUsageCheckPort {
    boolean isCategoryUsing(Long categoryId);
    boolean isAttributesUsing(Long attributeId);
}
