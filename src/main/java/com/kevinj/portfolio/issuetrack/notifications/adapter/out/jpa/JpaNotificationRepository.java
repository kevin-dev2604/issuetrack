package com.kevinj.portfolio.issuetrack.notifications.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {
}
