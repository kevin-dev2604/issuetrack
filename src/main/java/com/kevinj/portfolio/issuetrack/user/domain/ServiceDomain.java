package com.kevinj.portfolio.issuetrack.user.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceDomain {
    private Long userId;
    private String service;
    private YN isUse;
}
