package com.kevinj.portfolio.issuetrack.auth.application;

import com.kevinj.portfolio.issuetrack.auth.application.dto.*;
import com.kevinj.portfolio.issuetrack.auth.application.port.AuthPort;
import com.kevinj.portfolio.issuetrack.auth.application.port.LoginLogPort;
import com.kevinj.portfolio.issuetrack.auth.exception.PasswordDoNotMatchException;
import com.kevinj.portfolio.issuetrack.auth.exception.RefreshTokenInvalidException;
import com.kevinj.portfolio.issuetrack.auth.exception.UserNotFoundException;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements LoginUseCase, RefreshUseCase {

    private final UserPort userPort;
    private final AuthPort authPort;
    private final LoginLogPort logPort;

    @Override
    public LoginResponse login(LoginCommand loginCommand, String clientType) {

        User user = userPort.loadLoginUser(loginCommand.loginId())
                .orElseThrow(UserNotFoundException::new);

        try {
            if (!authPort.passwordMatches(loginCommand.loginPw(), user.getLoginPw())) {
                throw new PasswordDoNotMatchException();
            } else if (user.getIsUse().equals(YN.N)) {
                throw new UserNotFoundException();
            }

            logPort.recordSuccessLog(new LoginLogRecord(user, clientType));
        } catch (PasswordDoNotMatchException | UserNotFoundException e) {
            logPort.recordFailureLog(new LoginLogRecord(user, clientType));
            throw e;
        }
        return authPort.createLoginResponse(user);
    }

    @Override
    public void logout(RefreshCommand refreshCommand) {
        authPort.revokeToken(refreshCommand.refreshToken());
    }

    @Override
    public RefreshResponse refresh(RefreshCommand refreshCommand) {

        if (!authPort.validateRefreshToken(refreshCommand.refreshToken())) {
            throw new RefreshTokenInvalidException();
        }

        log.info("Refresh token input: " + refreshCommand.refreshToken());

        Long userId = authPort.revokeToken(refreshCommand.refreshToken());
        User user = userPort.loadById(userId)
                .orElseThrow(UserNotFoundException::new);

        return authPort.createNewToken(user);
    }
}