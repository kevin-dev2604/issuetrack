package com.kevinj.portfolio.issuetrack.user.application.port;

import com.kevinj.portfolio.issuetrack.user.domain.ServiceDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.util.Optional;

public interface UserPort {
    void create(User user);
    Optional<User> loadById(Long userId);
    Optional<User> loadLoginUser(String loginId);
    Optional<User> loadByEmail(String loginId);
    ServiceDomain loadServiceInfo(Long userId);
    void save(User user);
}
