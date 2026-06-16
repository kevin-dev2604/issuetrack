package com.kevinj.portfolio.issuetrack.user.adapter.out;

import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public void create(User user) {
        Users users = userMapper.toUsersEntity(user);
        jpaUserRepository.save(users);
    }

    @Override
    public Optional<User> loadById(Long userId) {
        return jpaUserRepository.findById(userId)
            .map(userMapper::toUserDomain);
    }

    @Override
    public Optional<User> loadLoginUser(String loginId) {
        return jpaUserRepository.findByLoginId(loginId)
            .map(userMapper::toUserDomain);
    }

    @Override
    public Optional<User> loadByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
            .map(userMapper::toUserDomain);
    }

    @Override
    public Optional<User> loadByProvider(String email, String provider) {
        return jpaUserRepository.findByEmailAndProvider(email, provider)
            .map(userMapper::toUserDomain);
    }

    @Override
    public void save(User user) {
        Optional<Users> users =  jpaUserRepository.findById(user.getUserId());

        users.ifPresent(usersEntity -> {
            usersEntity.update(user.getNickname(), user.getEmail(), user.getDetails(), user.getIsUse());
            jpaUserRepository.save(usersEntity);
        });
    }
}
