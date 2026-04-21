package com.kevinj.portfolio.issuetrack.auth.application.port;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;
import com.kevinj.portfolio.issuetrack.user.domain.User;

public interface AuthPort {

    // login
    boolean passwordMatches(String inputPw, String userPw);
    LoginResponse createLoginResponse(User user);

    // refresh
    boolean validateRefreshToken(String refreshToken);
    RefreshResponse createNewToken(User user);

    // logout, invlidate refreshToken
    Long revokeToken(String refreshToken);
}
