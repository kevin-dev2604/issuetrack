package com.kevinj.portfolio.issuetrack.user.adapter.out;

import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import com.kevinj.portfolio.issuetrack.user.domain.User;
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
            users.getIsUse(),
            users.getLoginFailCnt()
        );
    }
}
