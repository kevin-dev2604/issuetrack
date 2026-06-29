package com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.process;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.process.adapter.in.web.dto.step.StepInfoResponse;

import java.util.List;

public record ProcessInfoResponse(
        Long processId,
        String name,
        String note,
        YN isActive,
        List<StepInfoResponse> steps
) {
}
