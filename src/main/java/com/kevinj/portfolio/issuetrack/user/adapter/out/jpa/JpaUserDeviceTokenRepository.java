package com.kevinj.portfolio.issuetrack.user.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
    Optional<UserDeviceToken> findByUsersAndDeviceType(Users users, String deviceType);
    List<UserDeviceToken> findAllByUsers(Users users);
    void deleteByToken(String Token);
}
