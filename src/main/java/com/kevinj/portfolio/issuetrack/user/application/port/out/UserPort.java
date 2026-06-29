package com.kevinj.portfolio.issuetrack.user.application.port.out;

import com.kevinj.portfolio.issuetrack.user.domain.model.User;
import com.kevinj.portfolio.issuetrack.user.domain.model.UserDeviceTokenDomain;

import java.util.List;
import java.util.Optional;

public interface UserPort {
    void create(User user);
    Optional<User> loadById(Long userId);
    Optional<User> loadLoginUser(String loginId);
    Optional<User> loadByEmail(String loginId);
    Optional<User> loadByProvider(String email, String provider);
    void save(User user);
    void saveToken(User user, UserDeviceTokenDomain tokenDomain);
    Optional<UserDeviceTokenDomain> findToken(User user, String deviceType);
    List<String> findAllUserTokens(User user);
    void deleteToken(String token);
}
