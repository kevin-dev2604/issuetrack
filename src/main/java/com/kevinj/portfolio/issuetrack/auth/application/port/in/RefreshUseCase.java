package com.kevinj.portfolio.issuetrack.auth.application.port.in;

import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.RefreshCommand;
import com.kevinj.portfolio.issuetrack.auth.adapter.in.web.dto.RefreshResponse;

public interface RefreshUseCase {
    RefreshResponse refresh(RefreshCommand refreshCommand);
}
