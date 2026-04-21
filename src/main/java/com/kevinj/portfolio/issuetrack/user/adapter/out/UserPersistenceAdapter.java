package com.kevinj.portfolio.issuetrack.user.adapter.out;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.*;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.ServiceDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPort {

    private final JpaUserRepository jpaUserRepository;
    private final JpaServiceUsageRepository jpaServiceUsageRepository;
    private final UserMapper userMapper;
    private final UserServiceMapper serviceMapper;

    @Override
    public void create(User user) {
        Users users = userMapper.toUsersEntity(user);
        jpaUserRepository.save(users);

        ServiceUsage serviceUsage = serviceMapper.toServiceUsageEntity(users.getUserId());
        jpaServiceUsageRepository.save(serviceUsage);
    }

    @Override
    public Optional<User> loadById(Long userId) {
        Optional<Users> users =  jpaUserRepository.findById(userId);

        if (users.isEmpty() || !isServiceUser(userId)) {
            return Optional.empty();
        }

        return users.map(userMapper::toUserDomain);
    }

    @Override
    public Optional<User> loadLoginUser(String loginId) {
        Optional<Users> users =  jpaUserRepository.findByLoginId(loginId);

        if (users.isEmpty() || !isServiceUser(users.get().getUserId())) {
            return Optional.empty();
        }

        return users.map(userMapper::toUserDomain);
    }

    @Override
    public Optional<User> loadByEmail(String email) {
        Optional<Users> users =  jpaUserRepository.findByEmail(email);

        if (users.isEmpty() || !isServiceUser(users.get().getUserId())) {
            return Optional.empty();
        }

        return users.map(userMapper::toUserDomain);
    }

    @Override
    public ServiceDomain loadServiceInfo(Long userId) {
        ServiceUsageId serviceId = new ServiceUsageId(userId, serviceMapper.getServiceName());
        Optional<ServiceUsage> service = jpaServiceUsageRepository.findById(serviceId);

        return service.map(serviceMapper::toUserServiceDomain).orElse(null);
    }

    @Override
    public void save(User user) {
        Optional<Users> users =  jpaUserRepository.findById(user.getUserId());

        users.ifPresent(usersEntity -> {
            usersEntity.update(user.getNickname(), user.getEmail(), user.getDetails(), user.getIsUse());
            jpaUserRepository.save(usersEntity);
        });
    }

    private boolean isServiceUser(Long userId) {
        ServiceUsageId serviceId = new ServiceUsageId(userId, serviceMapper.getServiceName());
        Optional<ServiceUsage> service = jpaServiceUsageRepository.findById(serviceId);

        if (service.isEmpty()) {
            return false;
        }

        YN isUse = service.get().getIsUse();
        return isUse.equals(YN.Y);
    }
}
