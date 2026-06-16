package com.kevinj.portfolio.issuetrack.user;

import com.kevinj.portfolio.issuetrack.FakePort;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.util.HashMap;
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
