package com.kevinj.portfolio.issuetrack.user.application;

import com.kevinj.portfolio.issuetrack.user.application.dto.UserCreateCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserPasswordCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserUpdateCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserInfoResponse;
import com.kevinj.portfolio.issuetrack.user.domain.ServiceDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;

import java.util.Optional;

public interface UserUseCase {
    void signUp(UserCreateCommand command);
    void deleteUser(Long userId);
    void updateUser(Long userId, UserUpdateCommand command);
    void changePassword(Long userId, UserPasswordCommand command);
    UserInfoResponse getUserInfo(Long userId);

    // for login check
    ServiceDomain getServiceInfo(Long userId);
    Optional<User> getLoginUserDomain(String loginId);
    void addLoginFailCnt(Long userId);
}
