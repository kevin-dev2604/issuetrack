package com.kevinj.portfolio.issuetrack.notifications.adapter;

import com.kevinj.portfolio.issuetrack.notifications.adapter.out.jpa.Notification;
import com.kevinj.portfolio.issuetrack.notifications.domain.NotificationDomain;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;

public class NotificationMapper {

    public Notification toEntity(NotificationDomain domain, Users user) {
        return new Notification(
            domain.getNotificationId(),
            user,
            domain.getTitle(),
            domain.getMessage(),
            domain.getIsRead(),
            null
        );
    }

    public NotificationDomain toDomain(Notification entity) {
        return new NotificationDomain(
            entity.getNotificationId(),
            entity.getUser().getUserId(),
            entity.getTitle(),
            entity.getMessage(),
            entity.getIsRead(),
            entity.getCreatedAt()
        );
    }
}
