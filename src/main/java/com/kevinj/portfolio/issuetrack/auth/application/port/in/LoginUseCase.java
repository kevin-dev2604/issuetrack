package com.kevinj.portfolio.issuetrack.auth.application.port.in;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.RefreshCommand;

public interface LoginUseCase {
    LoginResponse login(LoginCommand loginCommand, String browserInfo);
    void logout(RefreshCommand refreshCommand);
}
