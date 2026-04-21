package com.kevinj.portfolio.issuetrack.process.application.dto.step;

public record StepCreateInfo(
        String name,
        Integer order
) {
    public boolean isValid() {
        if (name == null || name.isBlank()
                || order == null || order <= 0) {
            return false;
        }

        return true;
    }
}
