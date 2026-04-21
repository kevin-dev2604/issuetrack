package com.kevinj.portfolio.issuetrack.auth.application;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshCommand;

public interface LoginUseCase {
    LoginResponse login(LoginCommand loginCommand, String browserInfo);
    void logout(RefreshCommand refreshCommand);
}
