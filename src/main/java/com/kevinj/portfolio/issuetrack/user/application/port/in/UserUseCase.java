package com.kevinj.portfolio.issuetrack.user.application.port.in;

import com.kevinj.portfolio.issuetrack.user.adapter.in.web.dto.*;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;

import java.util.Optional;

public interface UserUseCase {
    void signUp(UserCreateCommand command);
    void deleteUser(Long userId);
    void updateUser(Long userId, UserUpdateCommand command);
    void changePassword(Long userId, UserPasswordCommand command);
    UserInfoResponse getUserInfo(Long userId);

    // for login check
    Optional<User> getLoginUserDomain(String loginId);
    void addLoginFailCnt(Long userId);

    // fcm token register
    void registerToken(Long userId, UserTokenCommand userTokenCommand);
}
