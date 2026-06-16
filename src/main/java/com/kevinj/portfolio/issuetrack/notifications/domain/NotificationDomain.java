package com.kevinj.portfolio.issuetrack.notifications.domain;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NotificationDomain {
    private Long notificationId;
    private Long userId;
    private String title;
    private String message;
    private YN isRead;
    private LocalDateTime createdAt;

    public NotificationDomain(Long userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.isRead = YN.N;
    }

    public void checkRead() {
        isRead = YN.Y;
    }
}
