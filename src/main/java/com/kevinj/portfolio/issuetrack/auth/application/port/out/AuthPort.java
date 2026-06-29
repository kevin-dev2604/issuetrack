package com.kevinj.portfolio.issuetrack.auth.application.port.out;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.RefreshResponse;
import com.kevinj.portfolio.issuetrack.user.domain.model.User;

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
