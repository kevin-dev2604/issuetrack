package com.kevinj.portfolio.issuetrack.auth.application.port.in;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.OAuth2LoginCommand;

public interface OAuth2LoginUseCase {
    LoginResponse loginOrRegister(OAuth2LoginCommand command);
}
