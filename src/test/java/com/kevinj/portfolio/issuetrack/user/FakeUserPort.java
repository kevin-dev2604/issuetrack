package com.kevinj.portfolio.issuetrack.user;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.user.application.port.out.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
import com.kevinj.portfolio.issuetrack.user.domain.model.UserDeviceTokenDomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeUserPort implements UserPort, FakePort {

    private final Map<Long, User> userList = new HashMap<>();

    @Override
    public void create(User user) {
        Long userId = newId();
        User madeUser = new User(
                userId,
                user.getLoginId(),
                user.getLoginPw(),
                user.getUserRole(),
                user.getNickname(),
                user.getEmail(),
                user.getDetails(),
                user.getProvider(),
                user.getIsUse(),
                user.getLoginFailCnt()
        );

        userList.put(userId, madeUser);
    }

    @Override
    public Optional<User> loadById(Long userId) {
        return Optional.ofNullable(userList.get(userId));
    }

    @Override
    public Optional<User> loadLoginUser(String loginId) {
        return userList.values()
                .stream()
                .filter(user -> user.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public Optional<User> loadByEmail(String email) {
        return userList.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public Optional<User> loadByProvider(String email, String provider) {
        throw new UnsupportedOperationException("Warning: Not supported in test environments (e.g., Fake Ports). Use only for the actual running application.");
    }

    @Override
    public void save(User user) {
        userList.put(user.getUserId(), user);
    }

    @Override
    public void saveToken(User user, UserDeviceTokenDomain tokenDomain) {
        // no action in fake port
    }

    @Override
    public Optional<UserDeviceTokenDomain> findToken(User user, String deviceType) {
        // no action in fake port
        return Optional.empty();
    }

    @Override
    public List<String> findAllUserTokens(User user) {
        // no action in fake port
        return List.of();
    }

    @Override
    public void deleteToken(String token) {
        // no action in fake port
    }


    @Override
    public Long newId() {
        return (long) (userList.size() + 1);
    }

    @Override
    public Long lastId() {
        return userList.keySet()
                .stream()
                .reduce(Long::max)
                .orElse(null);
    }
}
