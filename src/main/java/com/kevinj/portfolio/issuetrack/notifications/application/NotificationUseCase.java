package com.kevinj.portfolio.issuetrack.notifications.application;

public interface NotificationUseCase {
    void consumeDilemmaEvents();
    void consumeDiscussionEvents();
    void getNotificationList(Long userId);
}
