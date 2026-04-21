package com.kevinj.portfolio.issuetrack.auth.application;

import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;

public interface RefreshUseCase {
    RefreshResponse refresh(RefreshCommand refreshCommand);
}
