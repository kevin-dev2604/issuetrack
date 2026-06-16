package com.kevinj.portfolio.issuetrack.auth.application.usecase;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.OAuth2LoginCommand;

public interface OAuth2LoginUseCase {
    LoginResponse loginOrRegister(OAuth2LoginCommand command);
}
