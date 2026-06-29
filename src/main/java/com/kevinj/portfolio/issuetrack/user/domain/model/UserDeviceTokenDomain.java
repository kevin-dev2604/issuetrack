package com.kevinj.portfolio.issuetrack.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDeviceTokenDomain {

    private Long id;
    private Long userId;
    private String token;
    private String deviceType;
    private LocalDateTime lastLoggedInAt;

    public void update(String token, LocalDateTime lastLoggedInAt) {
        this.token = token;
        this.lastLoggedInAt = lastLoggedInAt;
    }
}
