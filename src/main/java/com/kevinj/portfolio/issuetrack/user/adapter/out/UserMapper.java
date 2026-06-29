package com.kevinj.portfolio.issuetrack.user.adapter.out;

import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.UserDeviceToken;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;
import com.kevinj.portfolio.issuetrack.user.domain.model.UserDeviceTokenDomain;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserMapper {
    public Users toUsersEntity(User user) {
        return new Users(
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
    }

    public User toUserDomain(Users users) {
        return new User(
            users.getUserId(),
            users.getLoginId(),
            users.getLoginPw(),
            users.getUserRole(),
            users.getNickname(),
            users.getEmail(),
            users.getDetails(),
            users.getProvider(),
            users.getIsUse(),
            users.getLoginFailCnt()
        );
    }

    public UserDeviceToken toTokenEntity(Users users, UserDeviceTokenDomain domain) {
        return new UserDeviceToken(
            users,
            domain.getToken(),
            domain.getDeviceType(),
            domain.getLastLoggedInAt()
        );
    }

    public UserDeviceTokenDomain toTokenDomain(UserDeviceToken entity) {
        return new UserDeviceTokenDomain(
            entity.getId(),
            entity.getUsers().getUserId(),
            entity.getToken(),
            entity.getDeviceType(),
            entity.getLastLoggedInAt()
        );
    }
}
